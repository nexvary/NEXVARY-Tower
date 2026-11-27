# Tower Guard release signing

The approved source is PR #3 at `4e5e81947a4c3f95a78cb284bf26e2db705a5fe8`. The package is `com.nexvary.imsi`. Version 0.7.0 keeps the approved in-app version text. The production candidate uses versionCode 9 and the approved Debug build uses 7.

## Establish the signing identity

1. Inspect each previously distributed APK with `apksigner verify --verbose --print-certs old.apk` and `aapt dump badging old.apk`. For an authorized connected test device, use `adb shell pm path com.nexvary.imsi` and pull the reported `base.apk`. Record the public certificate fingerprint and version code.
2. On the owner's trusted computer, inspect the existing keystore with `keytool -list -v -keystore /secure/path/to/keystore`. Verify the alias has a private-key entry and compare its fingerprint with the owner's release record and prior APKs. If the stored identity is missing, restore from the owner's backup.
3. A Debug APK and Release APK signed by different certificates cannot ordinarily update in place. Before distributing to existing users, verify compatibility with their actual prior APK. Preserve any existing installation and local data while resolving certificate incompatibility.

## Provision GitHub Actions

Create a `tower-guard-release` environment restricted to the exact branch `release/tower-guard-signed` and authorized reviewers where available. Restrict changes to the environment and workflow to authorized maintainers. Set these **environment secrets**:

| Secret | Value |
| --- | --- |
| `TOWER_KEYSTORE_BASE64` | Base64 of the encrypted keystore |
| `TOWER_STORE_PASSWORD` | Keystore password |
| `TOWER_KEY_ALIAS` | Private-key alias |
| `TOWER_KEY_PASSWORD` | Private-key password |

Keep the credentials out of Git, chat, logs, and build artifacts. From the trusted computer, an authenticated owner can stream the keystore directly to GitHub without creating a plaintext Base64 file:

```sh
base64 < /secure/path/tower-guard-release.p12 | gh secret set TOWER_KEYSTORE_BASE64 \
  --repo nexvary/NEXVARY-Tower --env tower-guard-release
```

Use the GitHub environment UI to enter the other three secrets. Set `TOWER_RELEASE_CERT_SHA256` as an environment **variable** using the public, 64-character hexadecimal certificate fingerprint without colons. Set the `TOWER_LAST_RELEASE_VERSION_CODE` environment variable to the highest version code previously distributed, using device/APK evidence and distribution records. Use 0 only if none exists. Compare results against the owner's independent fingerprint record before distributing any artifact.

## Build and verify

1. Confirm `releaseVersionCode` in `app/build.gradle` exceeds every previously distributed build. Once credentials and environment restrictions are in place, push a reviewed signing commit to `release/tower-guard-signed` to run **Tower Guard signed APK** without merging a development PR. Restrict the signing environment to that branch. Manual dispatch is also available once the workflow is on the default branch.
2. The quality job runs Release lint and the existing regression tests. The signing job uses the owner-controlled key in a temporary runner file, deletes that file on exit, and verifies both APKs against the owner's certificate fingerprint. The lower-version QA APK stays on the isolated runner.
3. The Android 15 emulator first installs and launches the Release APK from scratch. It then installs the same-key QA APK, runs `adb install -r` with the release, and verifies the existing installation timestamp, package identity, and a retained runtime permission. Before upgrading, it completes onboarding in the app UI. After upgrading, it verifies the app-created preference is byte-for-byte unchanged and the app opens the dashboard without showing onboarding again. This tests same-key Android upgrades. For compatibility with an older shipped APK, repeat on an isolated device or emulator with that exact APK, and verify retained app data. Do not use an owner's existing installation as a test target without approval.
4. Distribute only the APK from the final `NEXVARY-Tower-Guard-release-vc*` artifact after the workflow passes. Verify its SHA-256 with `sha256sum --check NEXVARY-Tower-Guard.apk.sha256` and inspect its signer with `apksigner verify --verbose --print-certs --Werr NEXVARY-Tower-Guard.apk`. After distribution, record the version code, certificate digest, APK SHA-256 and workflow link in the release record, and update `TOWER_LAST_RELEASE_VERSION_CODE` before the next release.

## Backup and recovery

Keep two access-controlled, encrypted offline copies of the original keystore in separate secure locations. Store recovery passwords separately in the owner's password vault. Record the alias, public fingerprint, certificate expiry, package ID and most recent distributed version code. GitHub Actions Secrets are for CI use and cannot serve as retrievable backups.

Test a restore from a backup on an isolated trusted computer. Confirm the alias is a private-key entry and its certificate digest matches the independent record. Restore the same key and certificate to the authorized GitHub environment if secrets are lost. If no private key or supported signing lineage can be recovered, pause distribution for existing installations and preserve their data.
