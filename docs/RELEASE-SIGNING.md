# Tower Guard release signing

The package is `com.nexvary.imsi`. The Android launcher label is **NEXVARY Tower Guard** in both locales. The release Gradle configuration uses the owner-controlled signing identity. The manual GitHub Actions workflow verifies the certificate fingerprint and RSA 4096-bit key before accepting any build.

## Establish the signing identity

1. On the owner's trusted computer, inspect the APK actually installed on the target device (or the exact APK originally installed) with `apksigner verify --verbose --print-certs old.apk`. For an authorized connected device, use `adb shell pm path com.nexvary.imsi` and pull the reported `base.apk`. Record the certificate SHA-256, key algorithm and size, and use `aapt dump badging old.apk` to record its version code. Repeat for each distributed signing identity.
2. Check owner-controlled archives, the original build machine, existing CI secret names, and Play Console App Integrity if applicable. Inspect any candidate with `keytool -list -v -keystore /secure/path/to/keystore`. Reuse a recoverable signing key only if the certificate matches the relevant installed APK, the entry includes the private key, and the key meets the RSA 4096-bit and validity requirements. A Play upload certificate and the app signing certificate serve different purposes.
3. If no qualifying key exists, get the owner's decision on legacy compatibility before provisioning a replacement. When approved, create a dedicated key on the owner's trusted computer. Choose a strong random password when prompted. PKCS12 uses the same password for the store and private key.

   ```sh
   umask 077
   keytool -genkeypair -storetype PKCS12 -keystore /secure/path/tower-guard-release.p12 \
     -alias tower-guard-release -keyalg RSA -keysize 4096 -sigalg SHA256withRSA \
     -validity 18262 -dname 'CN=NEXVARY Tower Guard, O=Nexvary Inc'
   ```

Android compares signing identities on update. If the installed APK has a different certificate, assess a supported signing lineage only if the old private key is recovered and the target Android versions and distribution path support it. A new key alone cannot update installations signed with an incompatible key. Keep those installations and their encrypted data in place until a migration is approved. Never uninstall or clear app data as part of release testing.

## Provision GitHub Actions

Create a `tower-guard-release` environment with access restricted to reviewed release refs and authorized reviewers where available. Restrict changes to the environment and workflow to authorized maintainers. Set these **environment secrets**:

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

1. Set `releaseVersionCode` in `app/build.gradle` above every previously distributed build and update its version name as appropriate. If the workflow is new, it must be available on the default branch for manual dispatch. Run **Tower Guard signed APK** from a reviewed ref allowed by the release environment.
2. The signing job creates a temporary keystore on the runner and deletes it on exit. It builds the release and a versionCode-minus-one QA APK with the same key. This QA APK is only for the isolated emulator.
3. The QA job installs the baseline, runs `adb install -r` with the release, and verifies the existing installation timestamp, package identity, and a retained runtime permission. This tests same-key Android upgrades. For compatibility with an older shipped APK, repeat on an isolated device or emulator with that exact APK, and verify retained app data. Do not use an owner's existing installation as a test target without approval.
4. Distribute only the APK from the final `NEXVARY-Tower-Guard-release-vc*` artifact after the workflow passes. Verify its SHA-256 with `sha256sum --check NEXVARY-Tower-Guard.apk.sha256` and inspect its signer with `apksigner verify --verbose --print-certs --Werr NEXVARY-Tower-Guard.apk`. After distribution, record the version code, certificate digest, APK SHA-256 and workflow link in the release record, and update `TOWER_LAST_RELEASE_VERSION_CODE` before the next release.

## Backup and recovery

Keep two access-controlled, encrypted offline copies of the original keystore in separate secure locations. Store recovery passwords separately in the owner's password vault. Record the alias, public fingerprint, certificate expiry, package ID and most recent distributed version code. GitHub Actions Secrets are for CI use and cannot serve as retrievable backups.

Test a restore from a backup on an isolated trusted computer. Confirm the alias is a private-key entry and its certificate digest matches the independent record. Restore the same key and certificate to the authorized GitHub environment if secrets are lost. If no private key or supported signing lineage can be recovered, pause distribution for existing installations and preserve their data.
