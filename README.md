# NEXVARY Tower Guard

Nexvary Inc. Android app for on-device awareness of cellular anomalies. The app reports changes in serving cells and radio measurements using a heuristic score. The score does not establish the presence of an IMSI catcher.

The development branch targets v1.0.0 and currently identifies itself as v0.7.0-alpha1. The application ID remains `com.nexvary.imsi`. The original v0.6.0 RC1 source is preserved on `main`.

ابدأ من [خطة التطوير](ROADMAP.md) لمعرفة الأعمال المتبقية قبل إصدار 1.0.0. يدعم التطبيق العربية والإنجليزية ويعالج قياسات الشبكة على الجهاز.

[Company website](https://nexvary.com) · [Support](https://nexvary.com/contact) · [Issue #1](https://github.com/nexvary/NEXVARY-Tower/issues/1)

## Development

## v0.4 additions
- First-run onboarding with a clear explanation of limitations and permissions.
- English / Arabic UI mode with RTL-aware layout direction.
- Dedicated encrypted scan history browser with per-scan detail.
- Trusted baseline management screen.
- In-app privacy policy.
- Production-minded Android backup / device-transfer exclusions.
- Existing v0.3 live foreground monitoring, RSRP/RSRQ timeline, neighbor comparison, explainable risk and PDF reports retained.

## Privacy / permissions
The manifest intentionally contains **no INTERNET permission**. The app requests `ACCESS_COARSE_LOCATION`, `ACCESS_FINE_LOCATION`, and `READ_PHONE_STATE` for Android CellInfo access. It does not attempt to read IMSI/IMEI. Local history/baselines are encrypted with AES-GCM using Android Keystore.

## Build
Use JDK 17 and Android SDK 35 with Build Tools 35.0.0. From the repository root run:

```sh
./gradlew :app:lintDebug :app:testDebugUnitTest :app:assembleDebug
```

On Windows use `gradlew.bat` with the same tasks. See [the development log](DEVELOPMENT_LOG.md) for what has been checked and [the test workflow](.github/workflows/android.yml) for the standalone timestamp tests. Test on physical devices before signing a release APK/AAB.

## Release checklist
1. Run unit/instrumentation tests and manual tests across multiple vendors/SIMs.
2. Run `./gradlew lintRelease` and dependency vulnerability review.
3. Use the original signing identity for compatible upgrades. Keep all signing material outside the repository.
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
