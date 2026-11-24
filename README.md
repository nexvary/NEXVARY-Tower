# NEXVARY IMSI Detector — v0.6 RC1

Release Candidate Android source for a privacy-first cellular anomaly awareness application.

## v0.4 additions
- First-run onboarding with a clear explanation of limitations and permissions.
- English / Arabic UI mode with RTL-aware layout direction.
- Dedicated encrypted scan history browser with per-scan detail.
- Trusted baseline management screen.
- In-app privacy policy.
- Production-minded Android backup / device-transfer exclusions.
- Existing v0.3 live foreground monitoring, RSRP/RSRQ timeline, neighbor comparison, explainable risk and PDF reports retained.

## Privacy / permissions
The manifest intentionally contains **no INTERNET permission**. The app requests only `ACCESS_FINE_LOCATION` and `READ_PHONE_STATE` for Android CellInfo access. It does not attempt to read IMSI/IMEI. Local history/baselines are encrypted with AES-GCM using Android Keystore.

## Build
Open in Android Studio with JDK 17 and Android SDK 35. Build and test on real devices before signing a release APK/AAB.

## Release checklist
1. Run unit/instrumentation tests and manual tests across multiple vendors/SIMs.
2. Run `./gradlew lintRelease` and dependency vulnerability review.
3. Generate a dedicated upload key and keep it out of the repository.
4. Build signed AAB for Google Play and signed APK for direct distribution.
5. Publish the web privacy policy and complete Google Play Data safety accurately.
6. Verify APK SHA-256 shown on nexvary.com after deployment.

This software provides anomaly indicators, not conclusive IMSI-catcher detection.


## RC1 maintenance update
- Added an in-app About page with NEXVARY ownership information and the official website: https://nexvary.com.
- No cellular-analysis logic or permissions were changed.


## v0.6 RC1 additions
- New NEXVARY IMSI Detector adaptive/legacy launcher icon.
- Correlated short-window anomaly scoring for repeated cell churn, radio-generation flapping, repeated 2G presence, and simultaneous cell/signal jumps.
- Assessment reasons are now stored with each encrypted local scan and shown in scan history.
- Existing UI, bottom navigation, privacy model, permissions and offline-only architecture preserved.

Important: these are anomaly indicators only, not proof of an IMSI catcher.
