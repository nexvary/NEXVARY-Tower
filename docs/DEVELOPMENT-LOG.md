# NEXVARY Tower Guard — Development Log

## Session 2026-11-27 (Premium Refinement — v0.7.0)

### Branch
`feature/tg01-tg11-implementation`

### Version
versionCode 7 / versionName 0.7.0

### Work completed this session

#### Screenshots / FLAG_SECURE (spec §2)
- Removed unconditional `FLAG_SECURE` from all 10 activities
- Created `SecureScreenUtil.java` — applies FLAG_SECURE only when user enables "Prevent Screenshots" in More → Settings
- Toggle stored in SharedPreferences; defaults to OFF (screenshots allowed)
- Applied consistently: OnboardingActivity, MainActivity, CellsActivity, HistoryActivity, ReportsActivity, MoreActivity, BaselinesActivity, PrivacyActivity, AboutActivity, SecurityAwarenessActivity

#### Back navigation (spec §3)
- Added `UiKit.backButton(Activity)` helper — renders back arrow with RTL/LTR awareness
- Injected back button at top of all secondary screens: SecurityAwarenessActivity, BaselinesActivity, PrivacyActivity, AboutActivity, AboutNexvaryActivity
- Added `onBackPressed() { finish(); }` overrides where missing
- Fixed nav tab index bugs: HistoryActivity was highlighting Cells (1) → fixed to History (2); BaselinesActivity was History (2) → fixed to More (4); PrivacyActivity was Reports (3) → fixed to More (4)
- Declared `android:parentActivityName` in manifest for all secondary activities

#### Premium visual redesign (spec §4)
- `colors.xml` updated: bg=#000000, panel=#050505, panel2=#0C0C0C, silver=#C0C0C0, platinum=#E5E7EB, green=#00E676, blue=#00B0FF, amber=#FFB300, red=#FF1744, violet=#9D4EDD
- `panel.xml` updated: solid #050505 with #2C2C2C border radius 10dp
- Added `panel_elevated.xml` (surface #0C0C0C, border #3A3A3A) and `panel_silver.xml` (metallic silver border)
- `button_primary.xml`: electric blue gradient with border
- `button_secondary.xml`: dark surface with silver border
- `UiKit.java`: page background → Color.BLACK, nav bar → #050505, eyebrow color → silver (#C0C0C0)

#### All application cards redesigned (spec §5)
- All cards use updated panel/panel_silver drawables with deep black surfaces and metallic borders
- Dashboard, Cells, History, Reports, More, SecurityAwareness, About — all inherit updated UiKit theme
- Compact padding preserved; five circular measurement gauges, cell transition graph, anomaly score maintained

#### Launcher icon (spec §6)
- `ic_launcher_foreground.xml` — new VectorDrawable: deep black shield, 3D metallic silver border, cellular tower mast, three signal arcs (electric blue, neon green, violet), amber antenna node
- Adaptive icon updated for foreground + background + monochrome (Android 15 themed icon)
- `ic_launcher.xml` and `ic_launcher_round.xml` updated

#### All internal icons replaced (spec §7)
- `ic_nav_home.xml` — multicolor: blue roof, silver walls, green door
- `ic_nav_cells.xml` — multicolor: silver mast, blue arc, green arc, amber node
- `ic_nav_history.xml` — multicolor: silver ring, blue hands, green center dot
- `ic_nav_reports.xml` — multicolor: silver document, blue lines, red PDF badge
- `ic_nav_info.xml` — multicolor: blue/green/amber dots (More)
- `ic_nav_baseline.xml` — multicolor: silver shield, green checkmark
- `ic_nav_privacy.xml` — multicolor: silver lock, blue shackle, green keyhole
- `ic_back.xml` — silver arrow
- `ic_social_facebook.xml` — official blue circle with white F
- `ic_social_x.xml` — dark circle with white X glyph
- `ic_social_youtube.xml` — red rounded rect with white play triangle
- `ic_social_website.xml` — electric blue globe
- `ic_social_email.xml` — amber envelope
- Dashboard social links updated to use ImageViews with real icons (no more text characters)
- Nav icons: color filter removed; alpha dimming for unselected state preserves multicolor

#### More screen restructured (spec §8)
- "USER GUIDE" renamed to "About Nexvary Inc." (Arabic: "عن Nexvary Inc.")
- Points to new `AboutNexvaryActivity`
- Company contact/links section removed from More screen (moved to About Nexvary Inc.)
- Security Knowledge Center retained as independent entry
- Screenshot prevention toggle added with CheckBox
- Compact layout: Language / Screenshot Prevention / Trusted Baselines / Privacy Policy / Security Knowledge Center / About Nexvary Inc.

#### About Nexvary Inc. screen created (spec §9)
- `AboutNexvaryActivity.java` — new premium bilingual company profile
- Logo + company name header card with panel_silver background
- Owner-approved Arabic description (verbatim per spec)
- English description
- Five contact cards: Website, Facebook, YouTube, X, Email — each with real multicolor icon, title, subtitle
- Back navigation with RTL-aware arrow direction

#### Company links verified (spec §10)
- All five links per AGENTS.md TG07 (owner-verified 2026-11-27):
  - https://nexvary.com/ — open via ACTION_VIEW
  - https://www.facebook.com/share/14p9krEn5ij/ — ACTION_VIEW
  - https://www.youtube.com/@NexvaryInc — ACTION_VIEW
  - https://x.com/Nexvary — ACTION_VIEW
  - info@nexvary.com — ACTION_SENDTO mailto:
- No INTERNET permission added; links handled by external browser/email app
- Graceful handling if no browser installed (LinkHelper catches ActivityNotFoundException)

#### Version bump
- versionCode: 6 → 7
- versionName: 0.6.0-rc1 → 0.7.0
- Version strings updated in AboutActivity and PrivacyActivity

### Build results
- `./gradlew lintDebug` — BUILD SUCCESSFUL
- `./gradlew assembleDebug` — BUILD SUCCESSFUL
- `./gradlew testDebugUnitTest` — NO-SOURCE (no unit tests in project; not a failure)
- APK: app/build/outputs/apk/debug/app-debug.apk
- APK size: 780K (debug)
- APK SHA-256: 1b73337c8b4f4ee6ffc1e6351267df4aaf71d897a49bbd7626dcbe501a49af36
- Build type: DEBUG (not a production-signed release)

### Physical device testing
- Real device (OPPO CPH2577 Android 15): PENDING — no device connected to build host
- Emulator test: not performed this session

### Known limitations / next actions
- Real device visual QA on OPPO CPH2577 (Android 15) outstanding
- Signed release APK requires owner's keystore (not available on build host)
- Landscape layout: tested conceptually, not on device
- mipmap-hdpi/mdpi/xhdpi/xxhdpi/xxxhdpi PNG launcher icons still use old PNGs — the adaptive icon replaces them on Android 8+; legacy PNG icons for older launchers should be regenerated from new vector
- CellsActivity SINR / PCI field population still depends on device CellInfo availability
