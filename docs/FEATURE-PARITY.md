# NEXVARY Tower Guard — Feature Parity / Acceptance Matrix
Last updated: 2026-11-27

| ID   | Requirement                              | Status    | Evidence / Notes |
|------|------------------------------------------|-----------|------------------|
| TG01 | Visual identity and dashboard            | Partial   | Deep black theme (#000000), metallic silver borders, multicolor icons, compact layout. Real device screenshot pending. |
| TG02 | Circular measurement row (5 gauges)      | Partial   | CircularMeasureView with real CellInfo values; updated color thresholds. Screenshot pending real device. |
| TG03 | Cell transition graph                    | Partial   | CellChangeView implemented; detail view pending. |
| TG04 | Anomaly gauge (0–100 circular)           | Partial   | AnomalyGaugeView; thresholds documented; score/severity/disclaimer visually separate. |
| TG05 | Cells and measurements screen            | Partial   | CellsActivity with multicolor back button; nav index fixed to 1 (Cells). |
| TG06 | Fixed bottom navigation (5 tabs)         | Partial   | Multicolor icons, no color filter, alpha dim for inactive; back nav fixes. |
| TG07 | Company identity and links               | Partial   | AboutNexvaryActivity with all 5 real multicolor icons; owner-verified links; dashboard icon strip. |
| TG08 | Localization and accessibility           | Partial   | Arabic/English throughout new screens; RTL-aware back arrow. TalkBack labels on icons. |
| TG09 | Privacy, security and performance        | Verified  | No INTERNET permission confirmed by lintDebug. FLAG_SECURE now optional (default OFF). AES-GCM/Keystore preserved. |
| TG10 | Build, QA and release                    | Partial   | lintDebug PASS; assembleDebug PASS; debug APK 780K SHA-256 1b73337c…; real device QA pending. |
| TG11 | Engineering workflow and acceptance      | Partial   | DEVELOPMENT-LOG.md and FEATURE-PARITY.md updated; branch and draft PR open. |

### New features this session
| Feature | Status | Notes |
|---------|--------|-------|
| Screenshot prevention setting | Verified | SecureScreenUtil; toggle in More; default OFF |
| Back navigation — all secondary screens | Verified | UiKit.backButton(); onBackPressed(); parentActivityName |
| Deep black theme (#000000) | Verified | colors.xml, panel.xml, UiKit all updated |
| Multicolor nav icons | Verified | No color filter; 7 new multicolor icons |
| Social media icons (real vectors) | Verified | 5 platform icons replacing text chars |
| New adaptive launcher icon (vector) | Verified | Shield + tower + signal arcs; adaptive + monochrome |
| AboutNexvaryActivity | Verified | Premium bilingual profile; 5 contact cards; owner-approved text |
| More screen restructured | Verified | Compact; "About Nexvary Inc." replaces "User Guide"; contact strip removed |
| Version bump v0.7.0 | Verified | versionCode 7 / versionName 0.7.0 |

### Status key
- **Verified** — implemented, built and confirmed by passing lint/build; code-level confidence high
- **Partial** — code written and committed; real device/emulator screenshot outstanding
- **Missing** — not yet started
- **Blocked** — depends on external resource (device, CI, owner approval)
