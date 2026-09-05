# Embedded trailers and ElegantFin-inspired theme experiment

This branch is an experimental continuation of Shield Enhanced V8.2. It does not replace the
V8.2 branch or its APK.

## Embedded YouTube trailers

- Remote YouTube trailer URLs open inside a full-screen Android system WebView.
- Playback uses the official YouTube IFrame Player API with the standard YouTube player,
  attribution, controls, captions, and advertising left intact.
- The app recognizes standard watch, short, embed, live, and Kodi-style YouTube trailer URLs.
- Local Jellyfin trailers continue to use Jellyfin's native player.
- Vimeo and other remote trailer providers retain the existing external-app behavior.
- If YouTube reports that a video cannot be embedded, the player opens the normal external
  YouTube URL as a fallback.
- Back returns to Jellyfin. Hardware media play/pause and rewind/fast-forward buttons are mapped
  to the IFrame player; Shield D-pad behavior still requires a real-device test.

The implementation follows YouTube's documented WebView approach by using `loadDataWithBaseURL`
to provide a referrer. The embedded player must remain unmodified and must not obscure YouTube's
controls or advertising.

## ElegantFin-inspired native theme

Settings > Customization > Theme now includes **ElegantFin inspired (experimental)**. It uses a
native dark navy gradient, violet focus/accent color, dark card surfaces, muted borders, and larger
corner radii based on the pinned ElegantFin color language. No ElegantFin CSS is loaded or copied;
Android TV cannot consume Jellyfin Web CSS.

This first pass changes the shared native theme tokens. Recreating the complete web layout will
require separate native work on the home rows and item-details screens.

## Build verification

Build an in-place debug upgrade with a version code above V8.2:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug '-Pjellyfin.version=0.0.0-dev.3'
```

Before installing on a real Shield, retain the V8.2 APK. Do not publish or install this experiment
until its APK identity, signature, checksum, and rollback path have been checked.
