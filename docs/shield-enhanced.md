# Jellyfin Android TV - Shield Enhanced

This branch contains an unofficial, experimental Jellyfin Android TV build developed and tested on an NVIDIA Shield TV. It is intended for sideloading and evaluation and is not an official Jellyfin release.

## Highlights

- Updated Media3 playback path with NVIDIA Shield Dolby Vision Profile 7 handling.
- Direct-play oriented playback behavior while preserving normal Jellyfin fallback behavior.
- Pause overlay with title artwork, playback timing, audio selection, subtitle selection, chapter selection, and playback speed controls.
- Human-readable audio track labels.
- Subtitle appearance controls that apply correctly to text subtitles.
- Five-across visual chapter browser with clear thumbnails, chapter names, timestamps, translucent backing, and a high-visibility focus border.
- Orange debug branding so the sideloaded build is easy to distinguish from the official app.

## Installation notes

The current artifact is a debug-signed APK using the package name `org.jellyfin.androidtv.debug`. It can coexist with the official Jellyfin Android TV application. Upgrades to later builds using the same signing identity and package name can be installed in place.

Install with Android Debug Bridge:

```text
adb install -r Jellyfin-AndroidTV-Shield-Enhanced-v0.1-rc1-debug.apk
```

## Scope and status

This build has been interactively tested on an NVIDIA Shield TV. Other Android TV devices, display modes, audio systems, and media combinations have not been comprehensively validated.

The debug APK is suitable for testing and personal sideloading. A broadly distributed release should use a dedicated release-signing key and a documented upgrade policy.

## Privacy

The source and build contain no server addresses, hostnames, account names, credentials, API keys, media-library names, or local development paths. Jellyfin server configuration remains in the application's private on-device data and is not embedded in the APK.

## Licensing and attribution

This is a modified build of Jellyfin Android TV and remains subject to the repository's GNU General Public License. Jellyfin names and artwork belong to their respective owners. This project is not endorsed by or affiliated with the Jellyfin project.
