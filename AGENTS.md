# AGENTS.md — NEXVARY Tower Guard

## Authority and scope
Owner: Nexvary Inc. Repository: https://github.com/nexvary/NEXVARY-Tower
Official website: https://nexvary.com
Product name: NEXVARY Tower Guard. Existing documented baseline: NEXVARY IMSI Detector v0.6 RC1.
This is the standing engineering specification for Claude, Work and other contributors. Requirements are targets, not claims of completed functionality. Inspect current HEAD, actual source, Gradle configuration and tests before implementation. Preserve existing work, application ID, package names, signing compatibility and encrypted data migrations. Do not rewrite the app unnecessarily.

## Mission and scientific integrity
Provide privacy-first, on-device cellular anomaly awareness, not definitive IMSI-catcher detection. Preserve existing correlated short-window scoring (cell churn, radio generation flapping, repeated 2G presence, simultaneous cell/signal jumps), explainable reasons, trusted baselines, foreground monitoring, encrypted history, PDF reports, onboarding and privacy policy. Never claim an anomaly proves interception or a fake base station. No active radio probing, interception, subscriber identification or collection of IMSI/IMEI. Distinguish unavailable measurements from zero. Never fabricate live data, tower coordinates or risk scores.

## TG01 — Visual identity and dashboard
Use deep glossy black, metallic silver borders, restrained neon green/blue/amber/red status accents, high contrast, genuine multicolor vector icons (licensed or original), compact cards and minimal whitespace. Avoid monochrome placeholders and redundant headings. Product header, monitoring status, circular measurements, cell-transition graph, anomaly gauge, compact cell details and fixed navigation form the primary screen. Follow the owner-approved visual concept; the image is a design reference, not evidence of a working UI. Use responsive Android layouts, system insets and readable text. No vertical scrolling on the normal-size primary dashboard at default font scale; move extended details to dedicated screens. For unusually small screens or large accessibility fonts, use adaptive layouts or accessible overflow rather than clipping. Test portrait and landscape where supported.

## TG02 — Circular measurement row
Place small circular gauges at the top for RSRP (dBm), RSRQ (dB), SINR (dB), RSSI (dBm), and network generation (2G/3G/4G/5G). Use actual Android Telephony/CellInfo readings, correct radio-specific fields, units and unavailable-value handling. Show N/A for unsupported or permission-denied values, distinguish stale samples, avoid false precision. Do not confuse LTE RSRP with RSRQ or NR SS/CSI measurements. Keep color thresholds documented and technology-specific; do not equate weak signal with an attack. Additional measurements belong in Cells details or compact adaptive gauges where space permits.

## TG03 — Cell transition graph
Implement an offline logical graph, not a geographic tower map. Show current serving cell, prior observed cells, directed transitions, timestamps, repeated transitions and anomaly-associated events. Use colored nodes, arrows and a compact recent-event timeline; distinguish serving-cell changes from neighbor observations. Derive events from actual observations and existing monitoring, deduplicate samples, bound history and memory, handle missing IDs, multi-SIM and radio changes without merging unrelated identities. Never infer physical tower coordinates or label a cell malicious from signal alone. Provide an expanded detail view and clear empty state.

## TG04 — Anomaly gauge
Show a circular 0–100 score connected to the existing risk engine, with documented green/amber/red severity thresholds, actual assessment reasons and a visible disclaimer in both languages. If existing score scale differs, document any mapping and preserve semantics. Distinguish insufficient evidence from low risk. Preserve existing correlated scoring and regression behavior; changes to algorithms require justification and tests. Do not generate artificial alarms.

## TG05 — Cells and measurements
Show available serving/neighbor information: Cell ID, PCI/PSC/BSIC as applicable, TAC/LAC, MCC/MNC, band, ARFCN/EARFCN/NR-ARFCN, network type and signal comparison. Only display supported fields with correct labels and units. Band derivation must be documented and validated, otherwise show unknown. Handle permission denial, airplane mode, no SIM, dual SIM, unavailable CellInfo and stale data gracefully. Do not request unnecessary permissions.

## TG06 — Fixed bottom navigation
Five tabs: Dashboard, Cells, History, Reports, More. Genuine colored icons with selected state and accessibility labels. Keep navigation fixed and respect gesture/system bars. Preserve monitoring across navigation without duplicate listeners. History: encrypted scan browsing, per-scan details and assessment reasons. Reports: preserve PDF generation and safe user-initiated sharing through FileProvider. More: Settings, trusted baselines, language, user guide, privacy policy, About System, About Developer, company contact links. Avoid duplicate action tiles where bottom navigation already provides access. Back navigation and state restoration must work.

## TG07 — Company identity and links
Use a compact strip of genuine Facebook, X/Twitter, YouTube, website and email icons on the dashboard if space permits, with full links in More/About. Verified website: https://nexvary.com. Facebook, X, YouTube and email are pending owner verification; use centralized configurable values, hide or disable unconfigured links, never invent accounts or addresses. Open HTTPS links through ACTION_VIEW and email through ACTION_SENDTO mailto; handle missing handlers safely. No embedded WebView, no automatic requests, no INTERNET permission merely for links. Use official brand assets in accordance with their usage terms. Do not disclose founder personal information.

## TG08 — Localization and accessibility
Arabic RTL and English LTR across all screens, resource-based strings, proper bidirectional isolation of identifiers and measurements, consistent units, no hardcoded untranslated labels. Test both locales, TalkBack descriptions, touch targets, contrast, large fonts and layout clipping. Color must not be the sole severity indicator.

## TG09 — Privacy, security and performance
Baseline manifest intentionally has no INTERNET permission; retain offline-only analysis. Existing ACCESS_FINE_LOCATION and READ_PHONE_STATE permissions must remain narrowly justified; inspect current manifest before changes. Preserve AES-GCM, Android Keystore, backup/device-transfer exclusions, non-exported internal activities and FileProvider access controls. No tracking, ads, analytics, cloud uploads or unnecessary permissions. Never log sensitive cell history or secrets. Handle lifecycle, process recreation, permission revocation and unavailable Keystore safely. Bound sample history and rendering work; avoid battery drain, excessive polling, UI-thread blocking and leaked listeners. No covert background collection. Preserve user control of monitoring and local data.

## TG10 — Build, QA and release
Inspect actual Gradle files; documented baseline uses JDK 17 and Android SDK 35. Use existing wrapper/build system. Run targeted unit tests, full regression suite as appropriate, lint and debug APK build. Verify risk engine, transition ordering/deduplication, unavailable metrics, locale direction, navigation, external intents, encryption compatibility and permission handling. Capture real screenshots in Arabic and English, normal phone size and small-screen/large-font cases; never present mockups as actual app output. Test real devices where available, especially OPPO CPH2577 Android 15; explicitly mark emulator-only or unavailable device tests. Verify no INTERNET permission in merged manifest. Publish debug APK as GitHub Actions artifact or approved prerelease asset, with SHA-256 and direct link; do not claim a signed production release. Never commit signing keys, credentials, local.properties or sensitive logs. Do not publish to Google Play without authorization.

## TG11 — Engineering workflow and acceptance
1. Read this file, README and relevant source; inspect HEAD and existing PRs.
2. Implement in dependency order: dashboard/gauges, graph/risk, navigation/details, links/localization, QA.
3. Reuse existing code and tests. Keep commits focused and preserve backwards compatibility.
4. Maintain docs/DEVELOPMENT-LOG.md and docs/FEATURE-PARITY.md (TG01–TG11, Verified/Partial/Missing/Blocked with evidence). Create them if absent. Record actual test results, screenshot paths, APK artifact, commit SHA, known limitations and next action.
5. Push verified work to a dedicated feature branch and open/update a draft PR. No merge without explicit owner approval. No force push or bypassing CI/security gates. If workflow permission blocks a push, export a patch and report once; do not repeat failed authentication.
6. Minimize token and Work quota consumption: read only relevant files, avoid repeated audits, redundant builds, prolonged CI polling and verbose progress reports. No paid services or unrelated repository changes. If limits approach, save and push verified work and provide a precise handover.

## Definition of done
Actual compact dashboard matching the visual direction; genuine live measurements and risk score; logical cell transition graph; five fixed tabs; working verified company links; Arabic/English layouts; preserved offline privacy and existing functionality; passing applicable tests and lint; real screenshots and downloadable debug APK. Unverified requirements must remain Partial or Blocked. No claims of completion based on static screens, mock data or documentation alone.
