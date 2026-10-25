# ☕ Caffeine Widget

**Keep your Android screen on with one tap.** A minimal home-screen widget that toggles your screen timeout — tap the cup to stay awake, tap again to restore normal timeout.

[⬇️ Download the APK](../../releases/latest) — about 45 KB. No Play Store needed.

## What it does

- **Tap ☕ (full cup, with steam)** — screen stays on indefinitely (Caffeine ON)
- **Tap ☕ (empty cup outline)** — screen timeout returns to your previous setting (Caffeine OFF)

Perfect for reading, cooking with a recipe on screen, presenting, using maps on a dash, or any time your phone keeps falling asleep on you.

## Features

- Tiny: **~45 KB** APK, pure Java, zero dependencies
- No ads, no trackers, no analytics, no internet permission
- No background services — battery impact is zero
- Remembers your original screen timeout and restores it exactly
- One permission only: *Modify system settings* (required to change screen timeout)

## Install

1. Download `caffeine-widget-1.0.apk` from the [Releases page](../../releases/latest)
2. Open the APK on your phone. If Android asks, allow "Install unknown apps" for your file manager/browser
3. Long-press your home screen → **Widgets** → find **Caffeine** → drag it anywhere
4. Tap it. The first time, Android will ask for *Modify system settings* — grant it, then tap again

## How it works

When ON, the widget sets Android's `SCREEN_OFF_TIMEOUT` to its maximum value, saving your previous timeout first. When OFF, it restores that saved value. Nothing runs in the background.

## Requirements

- Android 8.0+ (API 26)
- A home screen that supports widgets (all major launchers do)

## Privacy

This app has no internet permission and collects nothing. It stores only your enabled/disabled state and previous timeout value in local app preferences.

## Build from source


    ./gradlew assembleRelease

Signed with your own keystore (see keystore.properties).
