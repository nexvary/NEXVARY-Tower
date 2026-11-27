# NEXVARY Tower Guard — Feature Parity / Acceptance Matrix
Last updated: 2026-11-27

| ID   | Requirement                              | Status    | Evidence / Notes |
|------|------------------------------------------|-----------|------------------|
| TG01 | Visual identity and dashboard            | Partial   | Compact non-scrolling layout implemented; real screenshot pending APK build |
| TG02 | Circular measurement row (5 gauges)      | Partial   | CircularMeasureView implemented; real values populated from CellInfo; screenshot pending |
| TG03 | Cell transition graph                    | Partial   | CellChangeView enhanced with directed arrows and labels; detail view pending |
| TG04 | Anomaly gauge (0–100 circular)           | Partial   | AnomalyGaugeView implemented; thresholds documented in source; screenshot pending |
| TG05 | Cells and measurements screen            | Partial   | CellsActivity has serving and neighbor details. API-guarded NR identity and signal types compiled in the debug run. Live radio readings still need device QA. |
| TG06 | Fixed bottom navigation (5 tabs)         | Partial   | UiKit updated: Dashboard/Cells/History/Reports/More; ReportsActivity and MoreActivity added |
| TG07 | Company identity and links               | Partial   | LinkHelper with owner-verified links; strip on dashboard and full links in More |
| TG08 | Localization and accessibility           | Partial   | Arabic/English strings added for new screens; TalkBack labels set on new views |
| TG09 | Privacy, security and performance        | Partial   | Manifest inspection confirms no INTERNET permission. AES-GCM and screenshot protection remain in source. Runtime encrypted-data verification is pending. |
| TG10 | Build, QA and release                    | Partial   | [Debug lint and APK build passed](https://github.com/nexvary/NEXVARY-Tower/actions/runs/45938665558). Unit-test task had no sources. Signed emulator upgrade and screenshots pending. |
| TG11 | Engineering workflow and acceptance      | Partial   | DEVELOPMENT-LOG.md and FEATURE-PARITY.md created; branch and draft PR opened |

### Status key
- **Verified** — implemented, tested, evidence available
- **Partial** — code written and committed; real device/emulator test or screenshot outstanding
- **Missing** — not yet started
- **Blocked** — depends on external resource (device, CI, owner approval)
