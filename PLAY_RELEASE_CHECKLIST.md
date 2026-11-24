# Google Play / direct APK release checklist — v0.4 RC1

## Before signing
- Test on physical Samsung, Pixel, Xiaomi/OPPO-class devices and at least two Android API levels.
- Test with single-SIM and dual-SIM devices; verify permissions denied, revoked, and Location services disabled.
- Run `./gradlew lintRelease`, dependency review, and a mobile security assessment.
- Confirm no `INTERNET`, IMSI/IMEI, SMS, contacts, microphone, camera, or broad storage permissions appear in the merged release manifest.

## Signing
- Keep the upload/release keystore outside Git and outside the web root.
- Use a unique strong keystore password and back up the signing material securely.
- Build an AAB for Google Play and an APK for nexvary.com from the same reviewed commit.
- Record SHA-256 for the public APK and verify the website displays the same digest.

## Google Play declarations
- Publish the public privacy-policy URL from the NEXVARY site.
- Complete Data safety from the behavior of the final signed build, not from this checklist. The current RC is designed for on-device processing and has no INTERNET permission.
- Explain the use of precise Location permission: Android gates CellInfo behind this permission; the app does not intentionally store GPS coordinates.
- Do not claim guaranteed IMSI-catcher detection in store metadata; describe anomaly/risk awareness accurately.

## Store assets
- Final app icon, feature graphic, phone screenshots in English and Arabic.
- Short and full descriptions, support contact, privacy policy, version notes.
- Complete content rating and app access declarations.
