#!/usr/bin/env bash
set -euo pipefail

cd "${1:?Pass the directory containing the signed APKs}"

package=com.nexvary.imsi
launcher="$package/.OnboardingActivity"
prefs="/data/user/0/$package/shared_prefs/nexvary_ui.xml"

if [[ $(adb shell getprop ro.kernel.qemu) != 1 && $(adb shell getprop ro.boot.qemu) != 1 ]]; then
    echo 'This procedure requires an isolated Android emulator.' >&2
    exit 1
fi
[[ $(adb shell getprop ro.build.version.sdk) == 35 ]]
[[ -z $(adb shell pm path "$package") ]]

# This runner is an isolated Google APIs emulator. Root is for reading test
# preferences and is never used to modify app data.
adb root
adb wait-for-device
[[ $(adb shell id -u) == 0 ]]

open_dashboard() {
    adb shell am start -W -n "$launcher"
    for attempt in {1..10}; do
        adb shell dumpsys activity activities > activities.txt
        if grep -Eq "topResumedActivity=.*$package/.*MainActivity" activities.txt; then
            return 0
        fi
        sleep 1
    done
    return 1
}

complete_onboarding() {
    adb shell am start -W -n "$launcher"
    for attempt in {1..5}; do
        adb shell uiautomator dump /sdcard/tower-window.xml >/dev/null
        adb exec-out cat /sdcard/tower-window.xml > tower-window.xml
        point=$(python3 - <<'PY'
import re
import xml.etree.ElementTree as ET

for node in ET.parse("tower-window.xml").iter("node"):
    if node.get("text", "").strip().upper() in {"CONTINUE", "متابعة"} and node.get("enabled") == "true":
        x1, y1, x2, y2 = map(int, re.findall(r"\d+", node.attrib["bounds"]))
        if x2 - x1 > 24 and y2 - y1 > 24:
            print((x1 + x2) // 2, (y1 + y2) // 2)
        break
PY
    )
        if [[ -n "$point" ]]; then
            read -r x y <<< "$point"
            adb shell input tap "$x" "$y"
        fi
        if adb shell "grep -q 'onboarding_done' '$prefs' 2>/dev/null"; then
            return 0
        fi
        dimensions=$(adb shell wm size | sed -n 's/.*size: \([0-9]*\)x\([0-9]*\)/\1 \2/p' | tail -1)
        read -r width height <<< "$dimensions"
        adb shell input swipe "$((width / 2))" "$((height * 3 / 4))" "$((width / 2))" "$((height / 4))" 350
    done
    return 1
}

adb install -g NEXVARY-Tower-Guard.apk
complete_onboarding
open_dashboard
adb shell dumpsys package "$package" > fresh.txt
grep -E "^[[:space:]]+versionCode=$RELEASE_VERSION([[:space:]]|$)" fresh.txt
printf 'Android 15 emulator: fresh Release install, onboarding and dashboard passed.
' > installation-verification.txt
adb uninstall "$package"

adb install -g baseline.apk
complete_onboarding
adb exec-out cat "$prefs" > onboarding-before.xml
grep -F 'name="onboarding_done" value="true"' onboarding-before.xml
adb shell dumpsys package "$package" > before.txt

adb install -r NEXVARY-Tower-Guard.apk
adb exec-out cat "$prefs" > onboarding-after.xml
cmp onboarding-before.xml onboarding-after.xml
open_dashboard
adb shell dumpsys package "$package" > after.txt

for field in firstInstallTime 'userId|appId'; do
    before=$(grep -m1 -E "^ *($field)=" before.txt)
    grep -Fx "$before" after.txt
done
grep -E "^[[:space:]]+versionCode=$RELEASE_VERSION([[:space:]]|$)" after.txt
grep -F 'android.permission.READ_PHONE_STATE: granted=true' before.txt
grep -F 'android.permission.READ_PHONE_STATE: granted=true' after.txt
sha256sum --check NEXVARY-Tower-Guard.apk.sha256
printf 'Android 15 emulator: same-key QA APK upgraded in place, onboarding preference and phone permission retained, dashboard launched.\n' >> installation-verification.txt
cat installation-verification.txt
