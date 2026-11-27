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

## v0.7.0 — Final UI Corrections (2026-11-27)

### Nav icon fix
- `ic_nav_reports`: rewrote with clean separate `<path>` elements (bar-chart document); eliminated Z M subpath split that caused visual clipping

### More screen (compact redesign)
- Replaced full-width buttons with compact icon+label card rows (48dp min height, 24dp multicolor icons)
- Replaced `CheckBox` with `Switch` for screenshot prevention toggle
- Added section labels (SETTINGS / RESOURCES / INFORMATION)
- New icons: `ic_more_language`, `ic_more_screenshot`, `ic_more_baselines`, `ic_more_privacy`, `ic_more_skc`, `ic_more_about`

### Security Knowledge Center (SKC)
- Replaced A/B/C/D/E/F letter badges with six genuine multicolor `ImageView` icons: `ic_skc_cellular`, `ic_skc_interception`, `ic_skc_towerguard`, `ic_skc_alerts`, `ic_skc_protection`, `ic_skc_references`
- Replaced `▼`/`▲` text chars with `ic_chevron_down` rotated 180° on expand
- Replaced `›`/`∨` chars in article rows with `ic_chevron_right` rotated 90° on expand
- Background updated to `Color.BLACK`
- Reduced section gap from 8dp to 6dp

### No functional changes
- All article content preserved verbatim (bilingual AR/EN)
- No INTERNET permission added
- AES-GCM encryption unchanged
- PR #3 kept open and unmerged
