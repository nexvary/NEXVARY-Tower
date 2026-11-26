# NEXVARY Tower Guard — Feature Parity / Acceptance Matrix
Last updated: 2026-11-27

| ID   | Requirement                              | Status    | Evidence / Notes |
|------|------------------------------------------|-----------|------------------|
| TG01 | Visual identity and dashboard            | Partial   | Compact non-scrolling layout implemented; real screenshot pending APK build |
| TG02 | Circular measurement row (5 gauges)      | Partial   | CircularMeasureView implemented; real values populated from CellInfo; screenshot pending |
| TG03 | Cell transition graph                    | Partial   | CellChangeView enhanced with directed arrows and labels; detail view pending |
| TG04 | Anomaly gauge (0–100 circular)           | Partial   | AnomalyGaugeView implemented; thresholds documented in source; screenshot pending |
| TG05 | Cells and measurements screen            | Partial   | CellsActivity created with serving/neighbor table; PCI/ARFCN populated where available |
| TG06 | Fixed bottom navigation (5 tabs)         | Partial   | UiKit updated: Dashboard/Cells/History/Reports/More; ReportsActivity and MoreActivity added |
| TG07 | Company identity and links               | Partial   | LinkHelper with owner-verified links; strip on dashboard and full links in More |
| TG08 | Localization and accessibility           | Partial   | Arabic/English strings added for new screens; TalkBack labels set on new views |
| TG09 | Privacy, security and performance        | Verified  | No INTERNET permission; AES-GCM/Keystore preserved; FLAG_SECURE retained; no background collection |
| TG10 | Build, QA and release                    | Blocked   | No Android SDK on host; debug APK delegated to GitHub Actions; real screenshots pending |
| TG11 | Engineering workflow and acceptance      | Partial   | DEVELOPMENT-LOG.md and FEATURE-PARITY.md created; branch and draft PR opened |

### Status key
- **Verified** — implemented, tested, evidence available
- **Partial** — code written and committed; real device/emulator test or screenshot outstanding
- **Missing** — not yet started
- **Blocked** — depends on external resource (device, CI, owner approval)
