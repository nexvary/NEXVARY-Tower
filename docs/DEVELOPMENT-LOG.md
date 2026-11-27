# NEXVARY Tower Guard — Development Log

## Session 2026-11-27

### Branch
`feature/tg01-tg11-implementation`

### Commit
(pending push)

### Work done
- AGENTS.md updated: TG07 contact links owner-verified (Facebook, X, YouTube, email, website)
- `docs/DEVELOPMENT-LOG.md` and `docs/FEATURE-PARITY.md` created (TG11)
- `AnomalyGaugeView.java` — circular 0–100 score gauge replacing radar sweep (TG04)
- `CircularMeasureView.java` — compact circular dial for RSRP/RSRQ/SINR/RSSI/network row (TG02)
- `LinkHelper.java` — centralized company links via ACTION_VIEW / ACTION_SENDTO (TG07)
- `CellsActivity.java` + `activity_cells.xml` — serving/neighbor cell details screen (TG05)
- `ReportsActivity.java` — PDF export + local log access (TG06)
- `MoreActivity.java` + `activity_more.xml` — settings, baselines, language, links, about (TG06/TG07)
- `UiKit.java` — bottom nav updated to 5 tabs: Dashboard, Cells, History, Reports, More (TG06)
- `activity_main.xml` — compact non-scrolling dashboard with gauge row, graph+score panels (TG01)
- `AndroidManifest.xml` — CellsActivity, ReportsActivity, MoreActivity registered
- Strings updated for new screens (TG08)
- Amber color added for medium-risk threshold (TG04)

### Tests
- Unit/instrumentation tests: not run this session (no Android SDK on host). See TG10 — build delegated to GitHub Actions.
- Screenshots: pending APK build.

### APK
- Debug APK: pending GitHub Actions artifact.
- SHA-256: pending.

### Limitations / next actions
- Real device test on OPPO CPH2577 (Android 15): pending.
- Circular gauge color thresholds require field validation.
- Landscape orientation: layout tested only conceptually.
- CellsActivity SINR / PCI field population depends on device CellInfo availability.

## Permanent release signing

Prepared Gradle signing for the existing com.nexvary.imsi application and a manual release workflow with a pinned certificate fingerprint, APK checksum and isolated emulator upgrade gate. The release code is 9 and the emulator baseline is 8, above the highest version code observed in repository history (7). The owner must confirm the highest distributed build before signing. Existing English and Arabic launcher labels already use NEXVARY Tower Guard. See [release signing](RELEASE-SIGNING.md) for legacy certificate checks and recovery.

Local Android regression and release checks remain blocked. The Gradle wrapper could not download its distribution in this environment. A qualifying owner key has yet to be confirmed, and no APK or upgrade result is available.


## Android build recovery and release verification

- Fixed all four NR detail compilation errors with API 29 and runtime type checks, including unavailable long NCI and integer channel/signal values. Kept the installed package identity and the existing radio paths.
- Restored the repository's checksum-pinned Gradle wrapper. Fixed Android's paired coarse/fine location request without adding INTERNET. [Debug Actions](https://github.com/nexvary/NEXVARY-Tower/actions/runs/45938665558) passed lintDebug and assembleDebug. testDebugUnitTest reported NO-SOURCE. [Debug APK artifact](https://github.com/nexvary/NEXVARY-Tower/actions/runs/45938665558/artifacts/15171183760) SHA-256: `f1a9bdcc4d782c7fd6c8215689649429d1e093405abcaa156beccc8bc05d570a`.
- The signed release workflow checks that the release exceeds the owner-recorded distribution version and verifies an app-created onboarding preference before and after the Android 15 upgrade. Signing and emulator results are pending owner provisioning. No physical-device or screenshot result is recorded.
