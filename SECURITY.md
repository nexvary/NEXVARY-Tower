# Security — NEXVARY IMSI Detector v0.4 RC1

- No INTERNET permission in this release.
- AES-GCM local encryption backed by Android Keystore.
- `allowBackup=false` plus Android 12+ data-extraction exclusions.
- `usesCleartextTraffic=false`.
- App activities are non-exported except the launcher onboarding activity.
- FileProvider is non-exported and grants temporary share access only.
- `FLAG_SECURE` is used on screens that expose cellular telemetry.
- Live monitoring is foreground-only and stops in `onPause`.
- Do not commit signing keystores, passwords, Play credentials, or production crash-reporting secrets.
- Before release, test manifest/backup behavior and run static + dynamic application security review.
