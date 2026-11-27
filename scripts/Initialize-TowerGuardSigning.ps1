# Run in an interactive PowerShell on the owner's trusted Windows computer.
[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)] [string] $BackupDirectory1,
    [Parameter(Mandatory = $true)] [string] $BackupDirectory2,
    [Parameter(Mandatory = $true)] [ValidateRange(0, 2100000000)] [int] $HighestDistributedVersionCode,
    [string] $KeystoreDirectory = (Join-Path $env:LOCALAPPDATA 'Nexvary\TowerGuard\Signing'),
    [string] $KeytoolPath
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
$repo = 'nexvary/NEXVARY-Tower'
$environment = 'tower-guard-release'
$releaseBranch = 'release/tower-guard-signed'
$alias = 'tower-guard-release'
$endpoint = "repos/$repo/environments/$environment"
$fileName = 'tower-guard-release.p12'

function Invoke-Gh {
    $result = & gh @args
    if ($LASTEXITCODE -ne 0) { throw "GitHub CLI failed ($LASTEXITCODE)." }
    $result
}

function Set-EnvironmentSecret([string] $Name, [string] $Value) {
    $Value | & gh secret set $Name --repo $repo --env $environment
    if ($LASTEXITCODE -ne 0) { throw "Could not set $Name. Run setup again with the same keystore." }
}

if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw 'Install GitHub CLI from https://cli.github.com/ before running setup.'
}
& gh auth status --hostname github.com
if ($LASTEXITCODE -ne 0) {
    Invoke-Gh auth login --hostname github.com --web --git-protocol https
}
$login = Invoke-Gh api user --jq '.login'
if ($login -ine 'nexvary') { throw "GitHub CLI is signed in as $login. Switch to the authorized nexvary account." }

if (-not $KeytoolPath) {
    $keytoolCommand = Get-Command keytool.exe -ErrorAction SilentlyContinue
    if ($keytoolCommand) { $KeytoolPath = $keytoolCommand.Source }
    else { $KeytoolPath = Join-Path $env:ProgramFiles 'Android\Android Studio\jbr\bin\keytool.exe' }
}
if (-not (Test-Path -LiteralPath $KeytoolPath -PathType Leaf)) {
    throw 'Supply the full path to keytool.exe with -KeytoolPath.'
}

$backupOne = Get-Item -LiteralPath $BackupDirectory1
$backupTwo = Get-Item -LiteralPath $BackupDirectory2
if (-not $backupOne.PSIsContainer -or -not $backupTwo.PSIsContainer) {
    throw 'Both encrypted backup destinations must be existing directories.'
}
$primary = [IO.Path]::GetFullPath($KeystoreDirectory)
$roots = @($primary, $backupOne.FullName, $backupTwo.FullName) |
    ForEach-Object { [IO.Path]::GetPathRoot($_) } | Sort-Object -Unique
if (@($roots).Count -ne 3 -or @($roots | Where-Object { $_ -notmatch '^[A-Za-z]:\\$' }).Count -ne 0) {
    throw 'Use three distinct local drives for the primary copy and the two backup copies.'
}
$confirm = Read-Host 'Confirm the primary drive and both backup drives are encrypted, with backups on separate physical drives. Type YES'
if ($confirm -cne 'YES') { throw 'Setup stopped before creating a key.' }

$remoteEnvNames = @(Invoke-Gh api "repos/$repo/environments" --paginate --jq '.environments[].name')
$hasEnvironment = $environment -cin $remoteEnvNames
$remoteSecrets = @()
$remoteVariables = @{}
if ($hasEnvironment) {
    $remoteSecrets = @(Invoke-Gh secret list --repo $repo --env $environment --json name --jq '.[].name')
    $null = Invoke-Gh variable list --repo $repo --env $environment --json name,value |
        ConvertFrom-Json | ForEach-Object { $remoteVariables[$_.name] = $_.value }
}

New-Item -ItemType Directory -Path $primary -Force | Out-Null
$keystore = Join-Path $primary $fileName
$hasKey = Test-Path -LiteralPath $keystore -PathType Leaf
if (-not $hasKey) {
    if ($remoteSecrets.Count -gt 0 -or $remoteVariables.ContainsKey('TOWER_RELEASE_CERT_SHA256')) {
        throw 'The GitHub environment already has signing material. Restore the existing keystore locally before proceeding.'
    }
    foreach ($folder in @($backupOne, $backupTwo)) {
        if (Test-Path -LiteralPath (Join-Path $folder.FullName $fileName)) {
            throw 'An existing backup was found. Restore it locally instead of generating a replacement.'
        }
    }
}

$pass = Read-Host 'Enter the PKCS12 password privately. Use a strong random passphrase for a new key' -AsSecureString
$ptr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($pass)
try {
    $password = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($ptr)
    if ($password -notmatch '^[!-~]([ -~]*[!-~])?$') {
        throw 'Use printable ASCII characters with no leading or trailing spaces.'
    }
    if (-not $hasKey -and $password.Length -lt 20) { throw 'Choose a new passphrase with at least 20 characters.' }
    $env:TOWER_SETUP_TEMP_PASSWORD = $password
    try {
        if (-not $hasKey) {
            & $KeytoolPath -genkeypair -storetype PKCS12 -keystore $keystore -alias $alias -keyalg RSA -keysize 4096 -sigalg SHA256withRSA -validity 18262 -dname 'CN=NEXVARY Tower Guard, O=Nexvary Inc' -storepass:env TOWER_SETUP_TEMP_PASSWORD
            if ($LASTEXITCODE -ne 0) { throw 'Key generation failed.' }
        }
        $certificate = & $KeytoolPath '-J-Duser.language=en' '-J-Duser.country=US' -list -v -storetype PKCS12 -keystore $keystore -alias $alias -storepass:env TOWER_SETUP_TEMP_PASSWORD
        if ($LASTEXITCODE -ne 0) { throw 'Could not read the local release key.' }
    } finally {
        Remove-Item Env:TOWER_SETUP_TEMP_PASSWORD -ErrorAction SilentlyContinue
    }

    $details = $certificate -join "`n"
    $fingerprintMatch = [regex]::Match($details, '(?m)^\s*SHA256:\s*([A-Fa-f0-9:]+)\s*$')
    $fingerprint = $fingerprintMatch.Groups[1].Value.Replace(':','').ToLowerInvariant()
    if ($fingerprint -notmatch '^[0-9a-f]{64}$' -or $details -notmatch 'PrivateKeyEntry' -or $details -notmatch '4096[- ]bit RSA') {
        throw 'The keystore does not contain the expected RSA 4096-bit private-key entry.'
    }
    if ($remoteSecrets.Count -gt 0 -and -not $remoteVariables.ContainsKey('TOWER_RELEASE_CERT_SHA256')) {
        throw 'The GitHub environment has secrets without a recorded certificate fingerprint. Check the existing signing settings first.'
    }
    if ($remoteVariables.ContainsKey('TOWER_RELEASE_CERT_SHA256') -and
        $remoteVariables['TOWER_RELEASE_CERT_SHA256'] -ine $fingerprint) {
        throw 'The local fingerprint differs from the recorded release identity.'
    }
    $previousCode = $remoteVariables['TOWER_LAST_RELEASE_VERSION_CODE']
    if ($null -ne $previousCode -and $HighestDistributedVersionCode -lt [int] $previousCode) {
        throw 'The submitted versionCode is lower than the GitHub release record.'
    }

    $hash = (Get-FileHash -LiteralPath $keystore -Algorithm SHA256).Hash
    foreach ($folder in @($backupOne, $backupTwo)) {
        $backup = Join-Path $folder.FullName $fileName
        if (-not (Test-Path -LiteralPath $backup)) {
            [IO.File]::Copy($keystore, $backup, $false)
        }
        if ((Get-FileHash -LiteralPath $backup -Algorithm SHA256).Hash -ne $hash) {
            throw "The backup differs from the local key: $backup"
        }
    }
    if (-not $hasEnvironment) {
        $null = Invoke-Gh api --method PUT $endpoint --field 'deployment_branch_policy[protected_branches]=false' --field 'deployment_branch_policy[custom_branch_policies]=true'
        $null = Invoke-Gh api --method POST "$endpoint/deployment-branch-policies" --field "name=$releaseBranch" --field 'type=branch'
    }
    $policy = Invoke-Gh api $endpoint --jq '.deployment_branch_policy.custom_branch_policies'
    $rules = @(Invoke-Gh api "$endpoint/deployment-branch-policies" --paginate --jq '.branch_policies[] | [.name,.type] | @tsv')
    if ($policy -ne 'true' -or $rules.Count -ne 1 -or $rules[0] -cne "$releaseBranch`tbranch") {
        throw "Restrict the GitHub environment to the single branch $releaseBranch before provisioning secrets."
    }

    $null = Invoke-Gh variable set TOWER_RELEASE_CERT_SHA256 --repo $repo --env $environment --body $fingerprint
    $null = Invoke-Gh variable set TOWER_LAST_RELEASE_VERSION_CODE --repo $repo --env $environment --body "$HighestDistributedVersionCode"
    $base64 = [Convert]::ToBase64String([IO.File]::ReadAllBytes($keystore))
    Set-EnvironmentSecret TOWER_KEYSTORE_BASE64 $base64
    Set-EnvironmentSecret TOWER_STORE_PASSWORD $password
    Set-EnvironmentSecret TOWER_KEY_ALIAS $alias
    Set-EnvironmentSecret TOWER_KEY_PASSWORD $password

    Write-Host "Provisioned release signing in $repo / $environment."
    Write-Host "Public certificate SHA-256: $fingerprint"
    Write-Host "Highest distributed versionCode: $HighestDistributedVersionCode"
    Write-Host 'Two encrypted-drive backups match the local keystore. Keep the password in your vault.'
} finally {
    $base64 = $null
    $password = $null
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ptr)
    $pass.Dispose()
}
