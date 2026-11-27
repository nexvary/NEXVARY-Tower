#!/usr/bin/env bash
set -euo pipefail

package=com.nexvary.imsi
launcher="$package/.OnboardingActivity"
prefs="/data/user/0/$package/shared_prefs/nexvary_ui.xml"

# This runner is an isolated Google APIs emulator. Root is for reading test
# preferences and is never used to modify app data.
adb root
adb wait-for-device
[[ $(adb shell id -u) == 0 ]]
adb install -g baseline.apk
adb shell am start -W -n "$launcher"

# Complete actual onboarding through its UI and let SharedPreferences flush.
for attempt in {1..5}; do
    adb shell uiautomator dump /sdcard/tower-window.xml >/dev/null
    adb exec-out cat /sdcard/tower-window.xml > tower-window.xml
    point=$(python3 - <<'PY'
import re
import xml.etree.ElementTree as ET

for node in ET.parse("tower-window.xml").iter("node"):
    if node.get("text", "").strip().upper() == "CONTINUE" and node.get("enabled") == "true":
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
    if adb shell "test -f '$prefs'"; then
        break
    fi
    dimensions=$(adb shell wm size | sed -n 's/.*size: \([0-9]*\)x\([0-9]*\)/\1 \2/p' | tail -1)
    read -r width height <<< "$dimensions"
    adb shell input swipe "$((width / 2))" "$((height * 3 / 4))" "$((width / 2))" "$((height / 4))" 350
done
adb exec-out cat "$prefs" > onboarding-before.xml
grep -F 'name="onboarding_done" value="true"' onboarding-before.xml
adb shell dumpsys package "$package" > before.txt

adb install -r NEXVARY-Tower-Guard.apk
adb exec-out cat "$prefs" > onboarding-after.xml
cmp onboarding-before.xml onboarding-after.xml
adb shell am start -W -n "$launcher"
adb shell dumpsys activity activities > activities-after.txt
grep -E "topResumedActivity=.*$package/.*MainActivity" activities-after.txt
adb shell dumpsys package "$package" > after.txt

for field in firstInstallTime 'userId|appId'; do
    before=$(grep -m1 -E "^ *($field)=" before.txt)
    grep -Fx "$before" after.txt
done
grep -E "^[[:space:]]+versionCode=$RELEASE_VERSION([[:space:]]|$)" after.txt
grep -F 'android.permission.READ_PHONE_STATE: granted=true' before.txt
grep -F 'android.permission.READ_PHONE_STATE: granted=true' after.txt
sha256sum --check NEXVARY-Tower-Guard.apk.sha256
printf 'Verified byte-for-byte retention of the app-created onboarding preference and dashboard launch after update.\n' > upgrade-verification.txt
