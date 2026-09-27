# Shield Enhanced V9.0

An unofficial, experimental Jellyfin Android TV build for NVIDIA Shield TV. This is a prerelease of the separate debug app; it is not an official Jellyfin release.

## Highlights

- Refined TV home screen with generic cinematic artwork for the library categories.
- Continue Watching episodes show their series, season/episode number and episode title. Recent TV and music items provide routes to the series or artist overview.
- Removed the library media **Delete** action from item details and its More menu. The separate Live TV **Cancel series** scheduling action remains.
- In-app YouTube trailers use Left/Right for ten-second seeking and Select for play/pause. Back returns to Jellyfin. Standard YouTube player controls and attribution remain visible.
- Retains the Shield Enhanced playback interface, track controls, chapter browser and distinct orange debug branding from earlier versions.

## APK and verification

- APK: `Jellyfin-AndroidTV-Shield-Enhanced-V9.0-debug.apk`
- Package: `org.jellyfin.androidtv.debug` (can coexist with the official app)
- App version: `0.0.0-dev.30`, code `30`
- SHA-256: `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C`
- Signing: established debug certificate; SHA-256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`; APK signature v1/v2 verification passed.
- Offline build, 46 app unit tests and assembly passed. Lint is not clean: 81 errors, 326 warnings and 423 hints, with the same finding set as the prior dev29 candidate.
- Installed and launched on one NVIDIA Shield TV. The owner accepted the resulting TV and trailer-control experience. Broad device coverage, a full HDR/audio playback matrix, external trailer fallback and downgrade restoration were not tested for this prerelease.

## Install and updates

Install with `adb install -r Jellyfin-AndroidTV-Shield-Enhanced-V9.0-debug.apk`. Back up any app data you need before changing versions. This package is separate from the official Jellyfin app. An in-place upgrade from a build using the **same package name and signing certificate** should preserve its private app data; downgrading or changing signing keys may require a different procedure and was not verified here. Do not uninstall the debug app merely to retry an update: uninstalling deletes its private data.

This prerelease uses the existing debug signing identity to preserve in-place upgrades for current testers. It does not have automatic update support. Future APKs can update it in place only if they use the same package name, a higher version code and the same signing certificate. A future dedicated release key would require a separate migration plan.

## Source, license and attribution

The corresponding source is the `shield-enhanced-v9.0` tag in the `Smotheredhope80/jellyfin-androidtv` fork. This fork is based on Jellyfin Android TV and is distributed under GPL-2.0. Jellyfin names and artwork belong to their respective owners. This build is not endorsed or supported by the Jellyfin project.
