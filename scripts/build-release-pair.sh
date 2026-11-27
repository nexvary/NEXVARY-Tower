#!/usr/bin/env bash
set -euo pipefail

output="${1:?Pass a directory for the signed APKs}"
mkdir -p "$output"
./gradlew --no-daemon :app:assembleRelease -PtowerUpgradeBaseline
mv app/build/outputs/apk/release/app-release.apk "$output/baseline.apk"
./gradlew --no-daemon :app:assembleRelease
mv app/build/outputs/apk/release/app-release.apk "$output/NEXVARY-Tower-Guard.apk"
