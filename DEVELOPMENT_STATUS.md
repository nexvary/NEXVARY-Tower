# NEXVARY Tower Guard — 0.7.0-alpha1

This is a development snapshot, not a production release. Original source is preserved in the local main branch; changes are on feature/v1-production. Android applicationId remains com.nexvary.imsi. APK updates require the original signing key.

## Implemented
- Corporate About page, verified website links, email and GitHub, external intents with failure handling, local app privacy link.
- Consistent product name and runtime version on About and PDF reports.
- Static score gauge without fabricated tower coordinates or continuous animation.
- Joint coarse/fine location request and permission recheck.
- Reject stale cell readings (>30s); reject absent or ambiguous serving cell instead of scoring it as a threat; use serving-cell signal rather than the strongest neighbor.
- Restrict historical comparisons to 120 seconds; no risk penalty for missing neighbors or multiple registered cells.
- Android CI configuration (Gradle 8.9, JDK 17, SDK 35) and standalone timestamp tests.

## Evidence and limits
- 8 standalone JVM timestamp-policy cases passed with javac --release 17.
- 18 XML files parsed; git diff --check passed.
- Android build, lint, instrumentation, real device tests, signing and CI have NOT run. No new APK is provided.
- CellInfo still uses cached Android API readings; fresh asynchronous acquisition, subscription-specific collection, repeat-sample deduplication, confidence model, localization review, secure PDF lifecycle, background execution and retention controls remain open.
- Legacy score records are not recalculated. Heuristic score is not a calibrated probability or proof of a cell-site simulator. Multiple registered cells may be normal; the current conservative implementation may withhold a score in these situations.
- Repository was public and empty when checked; no source pushed. Set repository to private before import. Use the original local main branch and feature branch from the Git bundle; do not merge without review.
