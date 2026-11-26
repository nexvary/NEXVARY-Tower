# Roadmap to v1.0.0

Track work in [Issue #1](https://github.com/nexvary/NEXVARY-Tower/issues/1). Record completed work and verification in [DEVELOPMENT_LOG.md](DEVELOPMENT_LOG.md).

| Stage | Work | Acceptance |
| --- | --- | --- |
| Source import | Preserve the RC1 and alpha histories, add a verified Wrapper and documentation, and open a draft PR. Record the licensing decision with the owner. | Both refs are cloneable with the original commits reachable. |
| UI and metadata | Check About links, browser intents, version labels, Arabic RTL and English LTR. | Device screenshots and a manual verification checklist cover both languages. |
| Measurement reliability | Validate fresh asynchronous measurements, subscription attribution, deduplication, and confidence-aware anomaly handling. | Regression tests cover stale data, Dual SIM, ordinary handovers and insufficient evidence. |
| Privacy and lifecycle | Review permissions, Keystore and storage, PDF sharing/cache cleanup, retention, background use and battery. | Device tests verify permission denial, encrypted storage, upgrade compatibility and report lifecycle. |
| Release verification | Run unit and instrumentation suites, CI APK/AAB builds, and device checks across supported API levels including Android 15. | Record test results, applicable artifact hashes, screenshots and limitations. Obtain owner review before release or merge. |
