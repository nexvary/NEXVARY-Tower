# Development log

## v0.6.0 RC1 baseline

Original Android source preserved at `8c9672ce1a1c99a43b0b64bd0af8c97cc79c4340`.

## Phase 1 — v0.7.0-alpha1

Commit `d074f7b5f31cf2e08e284a5c925601526c0f792a` adds corporate About links, consistent branding and version metadata, conservative Android cell measurement handling, standalone timestamp tests, and Android CI. [Development status](DEVELOPMENT_STATUS.md) records the limits of this snapshot.

## 2026-11-26 — Repository preparation

- Verified both Git bundle copies and Git objects. The source ZIP matched all 63 files in the alpha snapshot.
- Added the verified Gradle 8.9 Wrapper, pinned the distribution checksum, and updated CI to validate and use the Wrapper.
- Documented contribution rules, current limitations, and the v1.0.0 plan. Android build and physical-device validation are tracked separately in [Issue #1](https://github.com/nexvary/NEXVARY-Tower/issues/1).
- Local verification passed for all 8 standalone timestamp cases and all 18 Android XML files. The local Android build remains unverified.
