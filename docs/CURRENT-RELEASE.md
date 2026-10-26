## V9.0 attended trailer remote run on Gary's Shield — 26 October 2026

Installed identity re-verified read-only: `0.0.0-dev.30`/code30, device APK SHA256 `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C` (unchanged V9.0). T02/U10 transport PASS by owner report; YouTube captions/settings NOT reachable with the dev30 mapping (PARTIAL). Lifecycle/Back and Delete-menu checks not itemized. No artifact, install or release-designation change. [T02 result](SHIELD-TEST-CATALOG.md#t02--trailer-media-keys-and-lifecycle).

## V9.0 Gary's Shield owner observation — 27 September 2026

For the installed V9.0/dev30 on Gary's Shield, Gary reports previously working behavior still worked in his checks, no visible Delete button, and no text showing behind logos. He will continue normal use and report issues. This is a physical owner observation for the paths tried, not a complete U09/U10, playback/HDR/audio, More-menu, fallback-trailer, or downgrade verification. APK identity and public release designation remain as recorded below; no new artifact or install was made. [Canonical handoff](HANDOFF.md).

## Cindy Shield V9.0 installed — 27 September 2026

Cindy's Shield at 192.168.68.85:5555 upgraded in place from dev27 to V9.0/dev30; installed version and SHA256 BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C verified. Launch succeeds but displays server setup; sign-in and general use confirmed by Gary; specific physical playback cases NOT RUN. No uninstall/data clear. Prior dev27 retained and hash-matched; downgrade untested. [Installation evidence](../../apk-inspection/20260927-cindy-v9-install/README.md), [task record](../../00_Project/ACTIVE-TASKS/20260927-cindy-v9-install.md), [pending overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-cindy-v9-installed.md). Earlier connection-blocked status is superseded. Next: record individual playback and remote-control cases if further qualification is needed.

Owner confirmation (2026-09-27 12:48 +01:00): Gary reports he signed in on Cindy's Shield and that it works like the other Shield. This confirms sign-in and general use by owner report; specific playback, trailer, remote-control and HDR/audio cases were not individually recorded. No new device command or APK change followed.

## Latest retained public prerelease: Shield Enhanced V9.0 — 27 September 2026

Published at [GitHub release shield-enhanced-v9.0](https://github.com/Smotheredhope80/jellyfin-androidtv/releases/tag/shield-enhanced-v9.0) as an unofficial prerelease. APK [local release record](../../release/shield-enhanced-v9.0/README.md): `Jellyfin-AndroidTV-Shield-Enhanced-V9.0-debug.apk`, 64,276,430 bytes, SHA256 `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C`; package `org.jellyfin.androidtv.debug`, version `0.0.0-dev.30`, code30; debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`, apksigner v1/v2 pass. Bytes match dev30 retained candidate and installed Gary Shield APK. Anonymous public APK download returned HTTP 200, 64,276,430 bytes and the same SHA256. GitHub API readback: draft=false, prerelease=true, two assets (APK and SHA256SUMS.txt).

Corresponding public source: tag `shield-enhanced-v9.0`, commit `7ad5526981afc025935c25a3d77fab3a455227f6`, branch `release/shield-enhanced-v9.0-public`. The complete app/playback and build-input Git trees match tested source commit `58064bcca4b66e90c8459ca74b020711065e3b7e`; private project docs and unrelated uncommitted `AGENTS.md` did not enter the public branch. Offline dev30 Gradle unit/lint/assembly passed, 46 app tests with zero failures/errors/skips. Lint is not clean: 81 errors, 326 warnings, 423 hints, same finding set as dev29. Gary accepted the installed TV/trailer experience in chat; wider device, HDR/audio playback, external trailer fallback, More-menu and downgrade/restore matrix remains unverified. V8.2 is the previous public prerelease. Debug signing/update limitations are in the public release notes; no dedicated release key or automatic update path.

## Dev30 installed experimental on Gary's Shield — 27 September 2026

`org.jellyfin.androidtv.debug` / `0.0.0-dev.30` / code30 installed in place on Gary's Shield `192.168.68.69:5555` at 10:53 BST after model/foreground/idle media preflight. Retained APK: `apk-inspection/20260927-dev30-trailer-shortcuts/jellyfin-androidtv-v0.0.0-dev.30-debug.apk`, 64,276,430 bytes, SHA256 `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C`; installed device APK hash matches. apksigner v1/v2 pass; established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Source: `experiment/mobile-inspired-tv-v12`, build base HEAD `209169cd1d501f1b8989d4f105b736a4bda6d71b` plus four modified app/test files in [hash manifest](../../apk-inspection/20260927-dev30-trailer-shortcuts/source-manifest.csv). Those exact source bytes were committed as `302cd8f67` after hash readback; unrelated `AGENTS.md` edit excluded. No push.

Offline app unit/lint/assembly passed in 5m57s: 46 tests, zero failures/errors/skips. Lint finding ID/severity/message multiset matches dev29 exactly: 81 errors, 326 warnings, 423 hints; build success does not mean clean lint. Installed startup and populated home verified by foreground readback and screenshot. Gary chose Left/Right ten-second seek and Select play/pause; physical trailer behavior and Back on dev30 await his observation, so U10 is not accepted yet. [Full candidate/install record](../../apk-inspection/20260927-dev30-trailer-shortcuts/README.md). V8.2 is the retained release; Cindy's Shield unchanged. Dev29 rollback APK SHA256 `3DF3BDC706A6819DB942DC6F1F9A282FD39E803149348B0DC3832D24076D5A2F` retained; downgrade/restore not exercised.

## Git preservation of accepted dev28 source — 27 September 2026

The 16 reviewed app files (code, tests and seven category banners) were committed as `1978b0f5103fc045f1ef16bf53d5dc07d34a2d78` on `experiment/mobile-inspired-tv-v12` after their working bytes matched the dev28 source manifest. The installed APK remains the separately verified 0.0.0-dev.28 artifact (SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E`); no rebuild or device action followed the commit. Source verification remains 45 passing app tests and the previously reviewed lint report. The independent `AGENTS.md` policy edit remains outside these commits. No upstream is configured for this branch and no push/integration has occurred. Earlier entries describing uncommitted source are dated pre-commit history.

## Dev28 owner acceptance — 27 September 2026

Gary reviewed the installed dev28 on his Shield and reported: **“Looks good. happy with this”**. Record this as owner acceptance of the delivered category artwork and UI changes. Installed artifact remains `0.0.0-dev.28` / code 28, SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E`. No additional device changes were made for this acceptance update.

The requested UI work and owner review are complete. This general acceptance does not supply individual U09 edge-case or playback regression results; those remain unrecorded. Cindy's Shield remains unchanged. Git changes remain checkpointed and uncommitted as previously documented; no release promotion or publication. Earlier next-action requests for Gary's initial review are superseded. Evidence: [acceptance record](../../apk-inspection/20260927-dev28-navigation/install-gary/acceptance/README.md).

## Dev28 installed on Gary's Shield — 27 September 2026

Authorized in chat and installed in place at 09:18 BST on Gary's Shield `192.168.68.69:5555` (SHIELD Android TV, Android 11). Preflight: launcher foreground, zero media sessions; installed dev27 hash matched the retained recovery APK. `adb install -r` succeeded. Device readback: `org.jellyfin.androidtv.debug`, `0.0.0-dev.28`, code 28; SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E` exactly matches the retained candidate below. Existing signing identity retained. Official package also present before upgrade; no uninstall or data clear.

Startup through exported StartupActivity succeeded; MainActivity foreground and screenshot confirmed populated home/library and new Collections banner. Initial direct MainActivity launch was rejected as non-exported; using the normal exported entry point resolved it. No playback started. Full U09 D-pad/Back, logo-transition, parent-navigation and playback acceptance NOT RUN; screenshot/startup are not physical acceptance. Cindy's Shield was not changed. V8.2 remains retained release; dev28 is installed experimental on Gary's Shield.

Evidence: [installation record](../../apk-inspection/20260927-dev28-navigation/install-gary/README.md), version/hash/install/startup logs and home.png. Prior dev27 retained as documented below; no downgrade/restore exercised. Next: Gary checks episode labels, recent TV series navigation, recent music artist navigation and artwork on TV. [Pending installation overview](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-dev28-installed-gary.md).

## Dev28 retained candidate — 27 September 2026 — NOT INSTALLED

APK: `apk-inspection/20260927-dev28-navigation/jellyfin-androidtv-v0.0.0-dev.28-debug.apk` (64,292,804 bytes), SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E`. Package `org.jellyfin.androidtv.debug`, version `0.0.0-dev.28`, code 28. apksigner exit 0, v1/v2 verified; certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13` matches the established identity. Verifier META-INF warnings retained in signature.txt.

Source: branch `experiment/mobile-inspired-tv-v12`, HEAD `9997386bf33a7225911f3d5459e38bab0c7d1385` plus uncommitted changes in the candidate's `source-checkpoint/` and `source-manifest.csv` (16 exact files). Seven approved A banners, episode identity, logo loading and series/artist navigation changes. Offline retained JDK21/SDK36 build passed with 45 app tests, zero failures/errors/skips. Lint remains 81 errors, 321 warnings, 423 hints; only the existing ConvertToWebp recommendation changes banner location. [Evidence, command and limits](../../apk-inspection/20260927-dev28-navigation/README.md).

Candidate only; physical U09/playback checks NOT RUN; no device or server changes. V8.2 remains retained release; dev27 remains last documented installed candidate, not queried today. Dev27 rollback artifact remains at `apk-inspection/20260915-dev27-music-text-episode-thumbs/jellyfin-androidtv-v0.0.0-dev.27-debug.apk`, SHA256 `CB87E715990C075A7F9953F7E0973ADBE1F2708B7F0805CDA61388C244D7C160`; verify actual target downgrade/data compatibility before rollback. Git disposition pending; exact local checkpoint retained, no NAS or restore claim.

## Git source baseline — 23 September 2026

The accumulated dev27-era native source, tests and library artwork were committed as `30326ff9dc60ea9fbdc5170161a210a6bb035cd2` on `experiment/mobile-inspired-tv-v12`. A pre-commit offline Gradle run of app/backend unit tests, lint and debug assembly at `0.0.0-dev.27` succeeded. The assembled APK SHA-256 was `CB87E715990C075A7F9953F7E0973ADBE1F2708B7F0805CDA61388C244D7C160`, identical to the retained dev27 candidate. App test XML records 39 passing tests; backend XML records five passing tests with its task up to date. Lint reports 81 errors, 321 warnings and 423 hints; it is not clean and no dev27 finding-by-finding baseline comparison was completed. Build log and source recovery evidence are linked from `HANDOFF.md`.

This commit records source provenance without designating a new release or changing installed identity. V8.2 remains the latest retained release; dev27 remains the last documented installed experimental build on Gary's and Cindy's Shields. No physical or live device verification occurred during Git cleanup, and installed state may have changed since the dated device records below.

## Dev27 installed on Gary's Shield — 15 September 2026

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was confirmed idle (`state=0`) prior to install.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.27` / code 27. Device APK SHA256 `cb87e715990c075a7f9953f7e0973adbe1f2708b7f0805cda61388c244d7c160` matches candidate APK `apk-inspection/20260915-dev27-music-text-episode-thumbs/jellyfin-androidtv-v0.0.0-dev.27-debug.apk` (67,583,603 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully; foreground confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Screenshots verified across all rows.

Dev27 enhancements:
1. **Individual Episode Thumbnails (Web App Parity)**: In `CardPresenter.kt`, TV show episodes in Next Up and Continue Watching rows now prioritize the individual episode scene thumbnail (`ep.itemImages[PRIMARY]`) rather than falling back to series/parent backdrop. Screen captures on Gary's Shield confirmed 100% parity with Jellyfin web app: *The Goldbergs* (Barry and Adam on couch), *The Shards* (party scene), *The IT Crowd* (Moss under table), *Forensic Files II* (burning bed), *Sabrina* (cake scene), *How I Met Your Mother* (Lily and Robin), *True Blood* (graveyard), and *Robot Chicken* (claymation Idol scene).
2. **Web App Parity Episode Text**: Top line displays the series name (e.g. "The Goldbergs", "How I Met Your Mother"), and subtitle displays `S{season}:E{episode} - {name}` (e.g. "S1:E4 - Why're You Hitting Yourself?", "S2:E2 - The Scorpion and the Toad") or remaining playback time during active progress.
3. **Music Row Bottom Text & Vertical Space**: Adjusted the home hero preview height from 236dp to 168dp in `ElegantFinHomePreview.kt` and `HomeFragment.kt`, providing 68dp (136px) of additional vertical clearance in `HomeRowsFragment`. Standardized music album covers to 130dp square in `CardPresenter.kt`, ensuring full visibility of both Title ("Massive Attack - Heligoland") and Subtitle ("10 tracks") with generous clearance above the bottom of the TV display.
4. **Marquee & Alignment**: Added `Modifier.fillMaxWidth()` to `ItemPreview` title and subtitle texts, ensuring text is properly centered and marquees smoothly on focus.

Installed status: Dev27 (`0.0.0-dev.27`, code 27) is installed experimental on both Gary's Shield (`192.168.68.69:5555`) and Cindy's Shield (`192.168.68.68:5555`). V8.2 remains latest retained release.
- **Gary's Shield (192.168.68.69:5555)**: Installed 2026-09-15 16:32 BST. APK SHA256 `cb87e715990c075a7f9953f7e0973adbe1f2708b7f0805cda61388c244d7c160` verified on-device.
- **Cindy's Shield (192.168.68.68:5555)**: Installed 2026-09-15 22:15 BST. Upgraded in-place from Dev18 (`code 18`) via `adb install -r`. Device dumpsys verified `versionCode=27` and `versionName=0.0.0-dev.27`, warm launch confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`.
- **Server Baseline**: Jellyfin Server on GARRO-NAS upgraded to stable `12.1.0` on 2026-09-16 22:10 BST; client compatibility verified.
Rollback resource: Dev26 (`apk-inspection/20260915-dev26-home-headers-webapp-parity/jellyfin-androidtv-v0.0.0-dev.26-debug.apk`, SHA256 `896f72edc0cb25c99890ce35b0eaa0ad4e4972152f2774b0abd9d7b511a94dec`), Dev25, Dev24, Dev23, Dev22, Dev20, Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260915-172500-music-text-episode-thumbs/`. Active local non-secret checkpoint; no NAS copy claim.
Overview updates:
- `Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev27-installed-gary-shield.md` (Status: applied by Homelab automatic updater, 2026-09-15T23:27:19+01:00, Master SHA256 `3A8C9539...`).
- `Knowledge/OVERVIEW-UPDATES/20260915-221500-jellyfin-dev27-installed-cindys-shield.md` (Status: applied by Hermes Agent, 2026-09-15T22:15:00+01:00).
Next: Gary and Cindy verify daily playback and browsing on Dev27 against Jellyfin Server 12.1.0.

## Dev25 installed on Gary's Shield — 15 September 2026

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was confirmed idle (`state=0`) prior to install.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.25` / code 25. Device APK SHA256 `fba1d417cef15d6f331768631533b6b9b4507d26e7b3797f0c48d3f15c199700` matches candidate APK `apk-inspection/20260915-dev25-tv-posters-clipping/jellyfin-androidtv-v0.0.0-dev.25-debug.apk` (67,587,419 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully; foreground confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Screenshot verified and saved to `../../backups/20260915-123500-home-tv-posters-clipping-fix/`.

Dev25 enhancements:
1. **TV Show Home Screen Poster Uniformity**: In `CardPresenter.kt`, enhanced episode aspect ratio and artwork resolution so when `modernHome && imageType == ImageType.POSTER`, episodes in Next Up and Continue Watching rows render as 2:3 vertical portrait posters (150dp height $\times$ 100dp width) displaying the series primary poster artwork (`parentImages[PRIMARY] ?: seriesPrimaryImage`). Live verification on Gary's Shield confirmed *The Goldbergs*, *The Shards*, *The IT Crowd*, *Forensic Files II*, *Sabrina, The Teenage Witch*, *How I Met Your Mother*, *True Blood*, *Archer*, and *Robot Chicken* render as vertical portrait posters, matching Movies.
2. **Hero Preview Artwork Clipping & Grid Alignment Fix**:
   - In `ElegantFinHomePreview.kt`: Standardized logo height to 48dp, compacted vertical spacing to `Arrangement.spacedBy(4.dp)`, set overview typography to `13.sp / 17.sp line height` (max 2 lines), and capped preview height to `236.dp + topInset`.
   - In `HomeFragment.kt`: Adjusted spacer height to `236.dp` and applied `.clipToBounds()` to `AndroidFragment<HomeRowsFragment>` to prevent scrolled rows from bleeding into the hero preview area.
   - In `HomeRowsFragment.kt`: Configured `verticalGridView` in `onViewCreated` with `WINDOW_ALIGN_LOW_EDGE`, offset 0, and offset percent disabled for ElegantFin, anchoring focused rows cleanly below the 236dp hero area. Live verification on Gary's Shield confirmed generous breathing room and zero vertical clipping between focused cards and the overview text, badges, and "Resume from..." line.

Installed status: Dev25 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev24 (`apk-inspection/20260915-dev24-popover-home-posters/jellyfin-androidtv-v0.0.0-dev.24-debug.apk`, SHA256 `72a520e7169839a4646d07b215be69eff8d26368757404196f79e522125e0e44`), Dev23, Dev22, Dev20, Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260915-123500-home-tv-posters-clipping-fix`. Active local non-secret checkpoint; no NAS copy claim.
Next: Gary verifies the updated home screen experience in daily use.

## Dev24 installed on Gary's Shield — 15 September 2026

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was confirmed idle (`state=0`) prior to install.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.24` / code 24. Device APK SHA256 `72a520e7169839a4646d07b215be69eff8d26368757404196f79e522125e0e44` matches candidate APK `apk-inspection/20260915-dev24-popover-home-posters/jellyfin-androidtv-v0.0.0-dev.24-debug.apk` (67,583,599 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully; foreground confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Screenshot verified and saved to `../../backups/20260915-053100-dev24-install/dev24-home.png`.

Dev24 enhancements:
1. **Home Screen Poster Consistency**: Fixed `HomeRowsFragment.kt` and `CardPresenter.kt` to use `ImageType.POSTER` with standard 150dp portrait height (100dp width) for movies and series in ElegantFin theme, while preserving 130dp height (231dp width) for 16:9 episode thumbnails. Fixed previous thumbnail forcing that caused movies to display widescreen landscape thumbnails or fallback inconsistently. Confirmed via screenshot (*Alien*, *Atomic Blonde*, *Batman*, *Harry Potter*, *Edward Scissorhands*, *Nightmare Before Christmas* now display clean, uniform 2:3 vertical posters).
2. **Audio Track & Subtitle Popover Wrapping**: In `VideoPlayerControls.kt` (`TrackSelectionPopover`), expanded width from 360dp to 520dp, height to 380dp max, changed item shape to `RoundedCornerShape(8.dp)`, aligned selection checkmarks to a dedicated 14dp column, and wrapped track text across up to 3 lines (`fontSize = 13.sp`, `lineHeight = 18.sp`, `TextOverflow.Ellipsis`). This eliminates horizontal clipping for detailed commentary and subtitle descriptions (e.g. *Alien* commentary with director, cast and crew credits).

Installed status: Dev24 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev23 (`apk-inspection/20260915-dev23-playback-hud/jellyfin-androidtv-v0.0.0-dev.23-debug.apk`, SHA256 `f32739d73e60ebf8996d537bed2fcd951b5358c1f1f01b88d01963a5dbcd91f7`), Dev22, Dev20, Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260915-052500-track-popover-home-posters`, `../../backups/20260915-053100-dev24-install`. Active local non-secret checkpoint; no NAS copy claim.
Next: Gary verifies audio commentary/subtitle track popovers during playback (e.g. on *Alien*) and confirms home screen poster appearance.

## Dev23 installed on Gary's Shield — 15 September 2026

Gary authorized installation of Dev23. Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was confirmed stopped (`state=0`) prior to install.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.23` / code 23. Device APK SHA256 `f32739d73e60ebf8996d537bed2fcd951b5358c1f1f01b88d01963a5dbcd91f7` matches candidate APK `apk-inspection/20260915-dev23-playback-hud/jellyfin-androidtv-v0.0.0-dev.23-debug.apk` (67,583,611 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully; foreground confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Screenshot verified and saved to `../../backups/20260915-051100-dev23-install/dev23-launch.png`.

Dev23 features:
1. **Landscape ElegantFin HUD Card**: Formatted as a 540dp landscape card with deep frosted navy surface (`Color(0xEE070A12)`), 14dp rounded corners, and glowing 1.5dp violet border (`Color(0x99775BF4)`).
2. **Top Header & Status Badges**: Features uppercase title `"SHIELD PLAYBACK HUD"` and play method pill badge (emerald `DIRECT PLAY`, cyan `DIRECT STREAM`, amber `TRANSCODING`).
3. **Structured 2-Column Key-Value Metrics**:
   - Video column: Codec, 4K UHD / 1080p / 720p resolution badge, exact dimensions, Dolby Vision / HDR10+ / HDR10 / SDR color badge, video bitrate, frame rate, and bit depth.
   - Audio & Container column: Container format, audio codec, Dolby Atmos badge, channel layout (`7.1 Surround`, `5.1 Surround`, `Stereo`), audio bitrate, sample rate.
   - Conditional transcoding warning box at bottom if active.
4. **Remote Back Dismissal**: Added `BackHandler(enabled = showPlaybackInfo) { showPlaybackInfo = false }` in `VideoPlayerOverlay.kt` so pressing Back on the Shield remote cleanly closes the HUD card without exiting video playback.

Installed status: Dev23 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev22 (`apk-inspection/20260914-dev22-browse-buttons/jellyfin-androidtv-v0.0.0-dev.22-debug.apk`, SHA256 `33454370bc3b728e6d9a78d78c119b80929eab2dd30f82d830b7d5769b7028d5`), Dev20, Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260915-045500-playback-stats-hud`, `../../backups/20260915-051100-dev23-install`. Active local non-secret checkpoint; no NAS copy claim.
Next: Gary starts playback and toggles the Info button to verify the landscape stats HUD card and remote Back dismissal.

## Dev22 installed on Gary's Shield — 15 September 2026

Gary reported two items in library browsing:
1. In Movies, TV Shows, and Music folders, the bottom Views row navigation buttons (*Suggested*, *All items*, *By letter*, *Genres*, *Albums*, *Artists*, *Random*, etc.) were square, grey, lacked semi-translucent styling, and had no violet outline, differing from the rest of the ElegantFin theme.
2. In the Music library, the background artwork flashed for a split second upon entering and then disappeared into black.

Dev22 resolves both issues:
- **GridButtonPresenter Modernization**: Modernized `GridButtonPresenter.kt` using Jetpack Compose with `RoundedCornerShape(12.dp)`, semi-translucent dark surface (70% idle, 35% violet accent on focus), glowing 2dp violet border (`R.color.theme_elegantfin_accent`) on focus with subtle 1dp glass border idle, standardized 44dp height and 136dp pill width, and centered 13sp semi-bold white typography.
- **Music Background Retention & Anti-Flicker**: Enhanced `BackgroundService.kt` with `fallbackItem` support and local library artwork resolution; updated `EnhancedBrowseFragment.java`, `BrowseGridFragment.java`, and `BrowseFolderFragment.kt` to preserve library backdrops when focusing non-media items (e.g. Views row or headers); added duplicate reload guard to eliminate rapid D-pad flickering.

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11).
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.22` / code 22. Device APK SHA256 `33454370bc3b728e6d9a78d78c119b80929eab2dd30f82d830b7d5769b7028d5` matches candidate APK `apk-inspection/20260914-dev22-browse-buttons/jellyfin-androidtv-v0.0.0-dev.22-debug.apk` (67,567,218 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`.
Live verification on Gary's Shield:
- Screen capture confirmed Music Views row (*Albums*, *Album artists*, *Artists*, *Genres*, *Random*) rendered in modern ElegantFin rounded styling with glowing violet focus outline on *Albums*.
- Verified Music backdrop remains stable and active without flashing or vanishing.
- Screen capture confirmed Movies Views row (*Suggested*, *All items*, *By letter*, *Genres*, *Random*) rendered in matching style with glowing violet focus outline on *Suggested*.

Installed status: Dev22 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev20 (`apk-inspection/20260914-dev20-player-buttons-badges/jellyfin-androidtv-v0.0.0-dev.20-debug.apk`, SHA256 `d66d8aa041c049849273f9d1672b96988fa2028348f998f358c7b88fed3a4f89`), Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260914-220000-browse-buttons-music-bg`. Active local non-secret checkpoint; no NAS copy claim.
Next: Gary verifies browsing buttons and music background in daily use, and reviews follow-up player HUD stats improvements.

## Rollback to Dev20 on Gary's Shield — 14 September 2026

Gary requested immediate rollback following testing of Dev21: the Rewind button with nested time indicators had expanded excessively ("HUGE go back 10 seconds button on the screen") and compressed the playback controls row down to a small play/pause button.
Action taken:
1. Media playback paused via ADB.
2. In-place rollback command `adb install -r -d jellyfin-androidtv-v0.0.0-dev.20-debug.apk` executed. Exit: `Success`.
3. Device package confirmed: `org.jellyfin.androidtv.debug` / `versionCode=20` / `versionName=0.0.0-dev.20`.
4. Device base.apk SHA256 verified: `d66d8aa041c049849273f9d1672b96988fa2028348f998f358c7b88fed3a4f89` (matches retained candidate Dev20 exactly).
5. Warm launch succeeded (`Status: ok`, TotalTime 1535ms). Launch and overlay screenshots verified and pulled to `../../backups/20260914-213500-revert-dev20/`.
6. Source tree restored to Dev20 state (`VideoPlayerControls.kt` and `PlaybackInfoOverlay.kt` restored from backup checkpoint).
7. Installed status: Gary's Shield is safely back on Dev20 (`0.0.0-dev.20`). Cindy's Shield remains untouched on Dev18.

## Dev21 installed on Gary's Shield — 14 September 2026

Gary verified the modern circular violet player controls on Dev20 and requested the following improvements:
1. **Redesigned Playback Stats HUD**: `PlaybackInfoOverlay.kt` completely overhauled from a narrow vertical plain-text column into a landscape HUD card (`width: 480.dp`) matching the mockup design. Styled with deep navy frosted glass container (`#F2070A12`), 12dp rounded corners, glowing violet border (`1.5.dp, Color(0x99775BF4)`), top header badge ("SHIELD PLAYBACK HUD" with emerald Direct Play `#10B981`, cyan Direct Stream `#06B6D4`, or amber Transcode `#F59E0B`), subtle violet divider, and clean 2-column key-value metrics with color-coded badges for Dolby Vision (lilac), HDR10/HDR (amber), and Dolby Atmos (cyan).
2. **Seek Button Time Indicators**: `VideoPlayerControls.kt` updated so `RewindButton` and `FastForwardButton` display nested `"10"` and `"30"` bold white indicators alongside their double-arrow icons.

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was paused during deployment.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.21` / code 21. Device APK SHA256 `656ed710d5ed87895c2dd64abbc43461eff95458941f8b249130cf7099505388` matches the candidate APK exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully (`Status: ok`, TotalTime 1540ms); process alive and verified foreground. Screenshot pulled to `../../backups/20260914-211500-playback-stats-hud/dev21-launch.png`.

Installed status: Dev21 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev20 (`apk-inspection/20260914-dev20-player-buttons-badges/jellyfin-androidtv-v0.0.0-dev.20-debug.apk`, SHA256 `d66d8aa041c049849273f9d1672b96988fa2028348f998f358c7b88fed3a4f89`), Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260914-211500-playback-stats-hud`. Active local non-secret checkpoint; no NAS copy claim.
Pending overview submission: `../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260914-jellyfin-dev21-installed-gary-shield.md`.
Next: Gary resumes playback (*Batman* or other media), toggles the Playback Info HUD, and checks the new violet landscape card and seek time indicators.

## Dev20 installed on Gary's Shield — 14 September 2026

Gary verified the HUD badges, violet seekbar, and chapter glow on Dev19, and requested two refinements:
1. **Modern Violet Playback Buttons**: `VideoPlayerControls.kt` updated so all 11 playback controls (Play/Pause, Rewind, Fast Forward, Chapters, Speed, Audio, Subtitles, Info, More, Prev, Next) use circular frosted glass styling with vibrant ElegantFin violet focused state (`#775BF4`) and lilac focus ring (`#D8B4FE`). Play/Pause configured as a standout hero button. Track popovers styled with violet focus states.
2. **Color-Coded Format HUD Badges**: `VideoPlayerHeader.kt` updated with vivid contextual color styling matching the design mockup: `4K UHD` (emerald green), `1080p` (sky blue), `Dolby Vision` (lilac violet), `HDR10+`/`HDR` (amber gold), `Dolby Atmos`/`TrueHD` (cyan / sky blue), and frosted silver metadata badges.

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was paused during deployment.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.20` / code 20. Device APK SHA256 `d66d8aa041c049849273f9d1672b96988fa2028348f998f358c7b88fed3a4f89` matches the candidate APK exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully (`Status: ok`, TotalTime 1727ms); process alive and verified foreground. Screenshot pulled to `../../backups/20260914-204700-player-buttons-badges/dev20-launch.png`.

Installed status: Dev20 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev19 (`apk-inspection/20260914-dev19-player-overlay/jellyfin-androidtv-v0.0.0-dev.19-debug.apk`) and Dev18 (`apk-inspection/20260912-dev18-search-artwork/jellyfin-androidtv-v0.0.0-dev.18-debug.apk`).
Evidence and targeted before-files: `../../backups/20260914-204700-player-buttons-badges`. Active local non-secret checkpoint; no NAS copy claim.
Pending overview submission: `../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260914-jellyfin-dev20-installed-gary-shield.md`.
Next: Gary resumes playback (*Harry Potter* or *Aliens*) and verifies the color-coded HUD badges and modern violet playback button controls.

## Dev19 installed on Gary's Shield — 14 September 2026

Gary authorized installation and physical playback HUD testing ("Ok test away"). Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Device was verified idle before deployment; no active media playback was running.

`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.19` / code 19. Device APK SHA256 `fe2076d9859686d99868458296b333b2f5e56de48eb4f32c037cb4a2094b1ecc` matches the candidate APK exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Explicit startup launch returned `Status: ok` (COLD, TotalTime 1805ms); process alive and verified foreground. Screenshot pulled to `../../backups/20260914-202400-dev19-install/dev19-launch.png`.

Installed status: Dev19 is now installed experimental on Gary's Shield (`192.168.68.69`). Cindy's Shield (`192.168.68.76`) remains untouched on Dev18 (`619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`). V8.2 remains latest retained release.
Rollback resource for Gary's Shield: Dev18 (`apk-inspection/20260912-dev18-search-artwork/jellyfin-androidtv-v0.0.0-dev.18-debug.apk`).
Evidence and targeted before-files: `../../backups/20260914-202400-dev19-install`, `../../backups/20260914-203000-dev19-doc-updates`. Active local non-secret checkpoint; no NAS copy claim.
Pending overview submission: `../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260914-jellyfin-dev19-installed-gary-shield.md`.
Next: Gary starts physical playback (*Aliens* or other media) on his Shield and checks modern HUD features (header badges, violet seekbar, active chapter timing, chapter strip, audio/subtitle selectors).

## Dev19 Modern Player Overlay candidate — 14 September 2026

Gary approved implementation of the Modern Native Player Overlay after reviewing the interactive browser mockup. Dev19 implements the ElegantFin player HUD natively in Jetpack Compose:
1. **Cinematic Navy Scrims**: `PlayerOverlayLayout.kt` updated with smooth vertical navy gradients (`Color(0xEE070A12)` to `Color(0xF2070A12)`), providing high contrast for text and controls while preserving center picture visibility.
2. **Top Header HUD Badges**: `VideoPlayerHeader.kt` now displays resolution (`4K`, `1080p`), color range (`Dolby Vision`, `HDR10+`, `HDR10`, `HLG`), audio format (`Dolby Atmos`, `TRUEHD`, `DTS-HD`, `EAC3`), production year, official content rating, and runtime duration badges under the clearlogo / title.
3. **Violet Accent Seekbar**: `PlayerSeekbar.kt` configured with `theme_elegantfin_accent` (#775BF4) played-track progress, white knob, buffer bar, and 6.dp height for TV distance viewing.
4. **Active Chapter & Timing HUD**: `PlaybackTimingRow` derives active chapter from `item.chapters` and playback position, presenting the current chapter title, remaining time, and calculated completion time.
5. **Popovers & Chapter Strip**: `TrackSelectionPopover` (audio/subtitles) styled with dark navy surface and border; `ChapterStrip` focused thumbnails highlighted with violet accent border and rounded corners.

Candidate APK: [dev19 APK](../../apk-inspection/20260914-dev19-player-overlay/jellyfin-androidtv-v0.0.0-dev.19-debug.apk), org.jellyfin.androidtv.debug / 0.0.0-dev.19 / code 19, 67,567,203 bytes. SHA256 FE2076D9859686D99868458296B333B2F5E56DE48EB4F32C037CB4A2094B1ECC. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; apksigner v1/v2 verified.
Build successful in 3m 8s; unit tests passed in 3m 29s. Zero new warnings in modified player files.
Before-files: `../../backups/20260914-195800-player-overlay-redesign`.

## Dev18 verified on both Shields — 14 September 2026

Gary confirmed completion of Option 1 and Option 2:
1. **Gary's Shield (`192.168.68.69`)**: Debug `0.0.0-dev.18` (code 18) installed in place (`adb install -r`) and verified. Dynamic Search input behavior verified: focusing into the search result group hides the query/input bar, preview expands to 184dp with query heading and two-line synopsis; D-pad Up to the Search toolbar button restores the search input field and re-requests keyboard focus. Movie poster outline clearance confirmed.
2. **Cindy's Shield (`192.168.68.76`)**: Side-by-side debug `0.0.0-dev.18` setup completed. Gary confirmed server authentication/sign-in, library browsing, and media playback acceptance passed. Official `org.jellyfin.androidtv` package remains retained alongside debug.

Installed status: `0.0.0-dev.18` (code 18) is now installed and verified on both household Shields. Retained candidate APK: `apk-inspection/20260912-dev18-search-artwork/jellyfin-androidtv-v0.0.0-dev.18-debug.apk`, SHA256 `619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4`. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`.
Rollback: Dev17 (`4FA55AE37C0E0B2F31F2B3296B0183D021AFE282CE2E2EE2D509453B7898F765`) remains retained prospective rollback for Gary's Shield; official app remains fallback for Cindy's Shield. V8.2 remains latest retained release.
Evidence and targeted before-files: `../../backups/20260914-194100-dev18-acceptance`. Active local non-secret checkpoint; no NAS copy claim.
Pending overview submission: `../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260914-jellyfin-dev18-verified-both-shields.md`.
Next: Option 3 — Modern Native Player Overlay Redesign (player controls, seekbar HUD, chapter browser, audio/subtitle selectors).

## Cindy's Shield: dev18 installed — 12 September 2026

Gary explicitly requested debug v18 on Cindy's Shield at 192.168.68.76:5555. Fresh ADB identity: NVIDIA SHIELD Android TV / Android 11, authorized. Before installation only official org.jellyfin.androidtv was present; no debug package or previous debug APK existed to preserve. Official Jellyfin media session was stopped (state 0, position 0, speed 0). No media command issued.

Retained candidate: apk-inspection/20260912-dev18-search-artwork/jellyfin-androidtv-v0.0.0-dev.18-debug.apk; 67,550,828 bytes; org.jellyfin.androidtv.debug / 0.0.0-dev.18 / code 18. SHA256 619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4; certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13. Fresh apksigner verification passed (META-INF protection warnings), aapt identity matched. Source provenance remains the candidate section/checkpoint below; no rebuild or source modification.

adb install -r returned Success. This was a new side-by-side debug installation, not an upgrade of the official package. Installed version/code and device-side SHA256 matched the candidate exactly. Both packages remain present. Explicit startup launch returned Status ok, COLD, 4885ms; later StartupActivity foreground and process alive. Authentication, signed-in library access, physical Search/focus/playback and wider regression acceptance NOT RUN. No playback started, data cleared, official app replaced, server changed or action on Gary's Shield.

Installed status above applies to Cindy's device only and supersedes candidate NOT INSTALLED for this device. Gary's installed version was not queried in this task. V8.2 remains latest retained release. Official app remains available as fallback; no prior debug version/data backup exists, and downgrade/data compatibility is untested. Do not uninstall or clear data without separate authorization.

Evidence/targeted document before-files: ../../backups/20260912-183850-cindy-dev18-install/installation.md. Active local non-secret checkpoint; NAS copy and restore testing not performed. Next: Cindy completes server/sign-in setup if prompted and checks requested Search behavior and playback. [Pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-cindy-dev18-installed.md); submission does not mean applied.

## Dev18 Search artwork/input candidate — 12 September 2026

Gary confirmed dev17 posters are no longer clipped; focused crop check USER-CONFIRMED PASS, full U05 not inferred. He requested hiding the search bar while browsing and reopening via the Search toolbar button to expose more artwork. Dev18 ElegantFin Search hides input/microphone/status on actual result-group focus with non-empty results; preview expands 128dp to 184dp, query is heading and two-line synopsis returns. Initial/input/no-result states retain controls. Search toolbar remains visible; Up from first result row explicitly targets Search, click restores query field and requests focus after a composition frame. Lower-row Up remains inside results. Adapter assignment guarded against same-instance rebinding during layout recomposition. Shared toolbar adds optional callback/requester; other callers/default theme retain behavior. No result clicks/playback handlers changed.

Candidate NOT INSTALLED: [dev18 APK](../../apk-inspection/20260912-dev18-search-artwork/jellyfin-androidtv-v0.0.0-dev.18-debug.apk), org.jellyfin.androidtv.debug / 0.0.0-dev.18 / code18, 67,550,828 bytes. SHA256 619A452510B527321AAE0B1FEC3162B6B523174F0A055A4986022CA40302E0F4. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; apksigner v1/v2, package and seven category images verified.

Build successful in 6m 47s; 39 fresh app tests pass, five unchanged backend tests UP-TO-DATE with passing XML. Lint 81 errors/317 warnings/423 hints: no new errors/warnings, one new AutoboxingStateCreation hint suggesting mutableIntStateOf for editRequest. Reviewed as allocation optimization, retained in candidate; not clean lint. Diff/code-resource checkpoint checks pass. HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 on experiment/mobile-inspired-tv-v12 plus retained source-tracked.patch/source-checkpoint/source-manifest.csv. Whitespace-only cleanup saved before compile completion; prior dirty work preserved, no commit/reset/branch change.

Physical input hide/reopen, focus/keyboard, selection retention, voice/Back/empty/failure and poster/artwork clearance checks NOT RUN; catalogue extended. App tests do not prove focus interop. Dev17 remains installed, retained SHA256 4FA55AE37C0E0B2F31F2B3296B0183D021AFE282CE2E2EE2D509453B7898F765 rechecked. V8.2 latest retained release. No device/playback/server action this pass. Before-files: ../../backups/20260912-182614-dev18-search-artwork, active local non-secret checkpoint, no NAS copy/restore-test claim. Restore mirrored files deliberately, preserving later work. Prospective downgrade/data compatibility untested. Next: user-authorized dev18 install and edit/browse/edit round trip; player overlay follows Search acceptance.
## Dev17 installed — 12 September 2026

Gary authorized installation and confirmed preference for posters. Fresh .69 target identified SHIELD Android TV with dev16/code16, installed hash matching retained final dev16 8A7F7AD64449E6DB9DB026216784552B396DEAD5F618AA57BD7C6F49B85A7369. Jellyfin already stopped (state0/speed0/position0); no media command issued.

adb install -r succeeded. Installed org.jellyfin.androidtv.debug / 0.0.0-dev.17 / code17, SHA256 4FA55AE37C0E0B2F31F2B3296B0183D021AFE282CE2E2EE2D509453B7898F765 matches candidate. Established candidate certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13, update accepted. Startup Status ok/COLD/4186ms. Screenshot shows Search input/placeholder/keyboard, no results query yet. Agent issued no browsing/typing commands. No uninstall/data clearing/server change or playback started. Private data not byte-compared.

Dev17 now installed experimental, superseding candidate NOT INSTALLED below. Install/hash/startup verified; poster clipping correction/result focus/labels/Back and broader U05/player regressions NOT RUN physically. Dev16-final retained prospective rollback, downgrade/data compatibility untested. V8.2 latest retained release. Evidence and targeted before-files: ../../backups/20260912-182154-dev17-install. Active local non-secret checkpoint, no NAS copy/restore-test claim. Next: Gary searches harry and checks full poster outline/category header/bottom labels.
## Dev17 Search poster-clearance candidate — 12 September 2026

Gary authorized the Search clipping correction and considered thumbnails while preferring posters. Posters retained. Search preview reduced 184dp to 128dp, logo 54dp to 44dp, repeated Search heading omitted and synopsis limited to one line. Other shared-preview callers keep default size/content. Search RowsSupportFragment explicitly aligns selected row 36dp from top, density converted, reserving header/focus clearance instead of percentage centering. Local retained Leanback 1.2.0 bytecode confirms setAlignment configures item/window offsets and stores alignment before view creation. Existing input/click/focus-exit behavior unchanged.

Candidate NOT INSTALLED: [dev17 APK](../../apk-inspection/20260912-dev17-search-spacing/jellyfin-androidtv-v0.0.0-dev.17-debug.apk). org.jellyfin.androidtv.debug / 0.0.0-dev.17 / code 17, 67,550,848 bytes. SHA256 4FA55AE37C0E0B2F31F2B3296B0183D021AFE282CE2E2EE2D509453B7898F765. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; apksigner v1/v2, identity and seven artwork assets verified.

Build successful in 6m, 39 fresh app tests pass; five backend tests UP-TO-DATE with passing XML, not fresh. Lint unchanged from final dev16: 81 errors/317 warnings/422 hints, no new findings, existing debt remains. Diff/code-resource checkpoint checks pass. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54, experiment/mobile-inspired-tv-v12 plus preserved dirty source patch/checkpoint/manifest. No commit/reset/branch change. Physical crop/focus outline/header/bottom labels/row scrolling/Back/default theme checks NOT RUN; U05 extended. Build does not prove clipping resolved.

Dev16-final remains installed; retained SHA256 8A7F7AD64449E6DB9DB026216784552B396DEAD5F618AA57BD7C6F49B85A7369 rechecked before build. V8.2 latest retained release. No device/playback/server actions this pass. Before-files: ../../backups/20260912-181049-dev17-search-spacing, active local non-secret checkpoint, no NAS copy/restore-test claim. Restore mirrored files only deliberately, preserving later work. Prospective downgrade/data compatibility untested. Next: user-authorized dev17 install and repeat query harry screenshot/focus check; player overlay follows Search qualification.
## Dev16 installed — 12 September 2026

Gary requested installation. Fresh .69 target identified as SHIELD Android TV with dev15/code 15 and installed hash matching retained DE2C4A924A1CAD5470591A708B373173E2757AA6EAD85F44F9CF6EBF2FE2A776. Active Jellyfin playback announced and cleanly stopped using MEDIA_STOP 86: state 0/speed 0/position 641537ms (10:41). Saved server progress/resumed scene not independently tested.

Final candidate adb install -r succeeded. Installed org.jellyfin.androidtv.debug / 0.0.0-dev.16 / code 16, SHA256 8A7F7AD64449E6DB9DB026216784552B396DEAD5F618AA57BD7C6F49B85A7369 matches FINAL candidate (not initial superseded dev16). Established candidate certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13, in-place update accepted. Startup Status ok/COLD/4171ms. First screenshot caught Movies rows loading; settled screenshot shows signed-in Movies browser with collection posters/Views controls. Collection row heading partly clipped at upper row boundary; layout acceptance not inferred. Agent issued no browsing commands after launch. No uninstall/data clearing/server change or agent-started playback. App-private data not byte-compared.

Dev16 now installed experimental, superseding final candidate NOT INSTALLED below. Install/hash/startup verified; U05 Search input/results/preview/focus/Back, shared preview and playback regressions NOT RUN. Dev15 retained prospective rollback; downgrade/data compatibility untested. V8.2 latest retained release. Evidence and targeted before-files: ../../backups/20260912-180426-dev16-install. Local active non-secret checkpoint, no NAS copy/restore-test claim. Next: Gary's Search check.
## Dev16 modern Search candidate — 12 September 2026

Gary authorized the Search stage after confirming dev15 logo transition works. Dev16 adds an ElegantFin navy Search screen, selected-result backdrop/logo/title/metadata/synopsis using the shared non-focusable 184dp preview, rounded high-contrast input with placeholder and violet border, and loading/empty/failure/partial/count messages. Existing grouped Leanback cards already provide violet focus styling. Keyboard/voice handlers and first-row Up boundary remain; click routes unchanged. Selection flow safely handles non-item rows, resets on new results, empty result groups omitted. Shared preview extracted without changing Movies/TV/collection layout or dev15 image-loading fallback.

Search request behavior: new query clears old rows; typing debounce stays 600ms; explicit same-query submit while job active starts immediately; same-query failures can retry. Failed category count is shown, result count is displayed entries (25-per-group cap and overlap remain), not unique media/server total. Default theme retains prior presentation, shared request lifecycle changes apply there too. English new labels explicitly use untranslated fallback; full localization pending.

FINAL candidate NOT INSTALLED: [dev16 APK](../../apk-inspection/20260912-dev16-search-final/jellyfin-androidtv-v0.0.0-dev.16-debug.apk), org.jellyfin.androidtv.debug / 0.0.0-dev.16 / code 16, 70,814,725 bytes, SHA256 8A7F7AD64449E6DB9DB026216784552B396DEAD5F618AA57BD7C6F49B85A7369. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; apksigner v1/v2, package and seven category PNGs verified. Initial APK at dev16-search is preserved as superseded validation artifact, NOT installation candidate (count-label lint correction).

Implementation build successful 5m 39s with 39 fresh app tests passing. Final resource-label build successful 4m 3s, tests UP-TO-DATE with passing XML; five backend tests also unchanged/cached. Final lint 81 errors/317 warnings/422 hints, no new findings vs dev15; existing debt remains. Source/diff checks passed. No new automated Search focus/network tests; unit suite does not prove the newly added remote/input/request flows. U05 physical keyboard/voice/remote focus/Back/layout/loading/empty/failure/partial/retry and shared-preview regressions NOT RUN. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54, experiment/mobile-inspired-tv-v12 plus full dirty patch/checkpoint/hash manifest in final candidate directory. No commit/reset/branch change.

Dev15 remains installed; retained SHA256 DE2C4A924A1CAD5470591A708B373173E2757AA6EAD85F44F9CF6EBF2FE2A776 rechecked. V8.2 latest retained release. No device/playback/server actions. Before-files: ../../backups/20260912-174753-dev16-search, active local non-secret checkpoint, no NAS copy/restore-test claim. Restore mirrored source/documents deliberately and remove new search_status.xml only for source rollback, preserving later work. Prospective dev15 downgrade/data compatibility untested. Next: user-authorized final dev16 installation and U05 acceptance; player overlay remains next UI stage.
## Dev15 installed — 12 September 2026

Gary requested installation. Fresh target .69 identified as SHIELD Android TV with dev14/code 14; installed SHA256 matched retained FAFF1E6BDC93F4B4D4FFAC694293FCE3592ACBC144875D9F0E243490686CA108. Playback already stopped, state 0/speed 0/position 781376ms; no media command issued.

adb install -r succeeded. Installed org.jellyfin.androidtv.debug / 0.0.0-dev.15 / code 15, SHA256 DE2C4A924A1CAD5470591A708B373173E2757AA6EAD85F44F9CF6EBF2FE2A776 matches candidate. Established candidate certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13, update accepted. Startup Status ok/COLD/4453ms. Signed-in Home observed with Harry Potter and the Philosopher's Stone selected, logo and Resume from 13:01 visible. No uninstall/data clearing/server change or agent-started playback. Private data not byte-compared; resumed scene not tested.

Dev15 installed experimental, superseding candidate NOT INSTALLED below. Installation and Home startup verified; collection cold/cached logo transition, missing/failed artwork fallback and playback regressions NOT RUN. Dev14 retained prospective rollback, downgrade/data compatibility untested; V8.2 latest retained release.

Evidence and targeted before-files: ../../backups/20260912-173733-dev15-install. Local active non-secret checkpoint, no NAS copy/restore-test claim. Next: Gary repeats Harry Potter collection logo-loading check.
## Dev15 logo-loading candidate — 12 September 2026

Gary tested the Harry Potter collection on dev14: logos appear, but a written title briefly pops in first. This confirms collection logo display, with a loading presentation issue; broader acceptance is not inferred. Source showed every non-success Coil state rendered text. Dev15 retains the fixed 54dp logo space during Empty/Loading for a known URL, displays logo on Success, and falls back to title for no URL or Error. Keyed item composition continues to prevent previous-item logo retention. Shared Movies/TV/collection preview only; no player/server change.

Candidate NOT INSTALLED: org.jellyfin.androidtv.debug / 0.0.0-dev.15 / code 15, 67,548,481 bytes. SHA256 DE2C4A924A1CAD5470591A708B373173E2757AA6EAD85F44F9CF6EBF2FE2A776. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; apksigner v1/v2, package identity and seven category PNGs verified. [Candidate evidence](../../apk-inspection/20260912-dev15-logo-loading/README.md).

Build successful in 5m 32s; 39 fresh app tests pass. Five unchanged backend tests UP-TO-DATE with passing XML, not fresh. Lint unchanged from dev14: 81 errors/318 warnings/422 hints; no new findings, not clean lint. Diff/source checkpoint checks passed. Physical cold/cached/rapid logo loading, missing/failed image fallback and Back checks NOT RUN; added catalogue procedure. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 on experiment/mobile-inspired-tv-v12 plus retained source-tracked.patch/source-checkpoint/source-manifest.csv. Prior dirty work preserved, no commit/reset/branch change.

Dev14 remains installed; retained SHA256 FAFF1E6BDC93F4B4D4FFAC694293FCE3592ACBC144875D9F0E243490686CA108 rechecked before build. V8.2 remains latest retained release. No device/playback/server action this pass. Targeted before-files: ../../backups/20260912-172021-dev15-logo-loading, local active non-secret checkpoint with no NAS copy/restore-test claim. Restore mirrored source/documents only deliberately, preserving later work. Prospective APK downgrade/data compatibility untested. Next: user-authorized dev15 install and Harry Potter cold/cached/fallback visual check.
## Dev14 installed — 12 September 2026

Gary requested installation. Fresh target .69 identified as SHIELD Android TV with dev13/code 13; installed dev13 SHA256 matched retained 5E6F7D258B53D8F5BC26E74C335675F340F29C1BDE9F8CB0E30F943F5221D51C. Active Jellyfin playback was announced and cleanly stopped using MEDIA_STOP 86: state 0, speed 0, position 265154ms (4:25). Saved server progress/resumed-scene accuracy not independently tested.

adb install -r succeeded. Installed org.jellyfin.androidtv.debug / 0.0.0-dev.14 / code 14, SHA256 FAFF1E6BDC93F4B4D4FFAC694293FCE3592ACBC144875D9F0E243490686CA108 matches candidate. Established candidate certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13, update accepted. Startup Status ok/COLD/4044ms; signed-in Home with category artwork and Continue Watching observed. No uninstall/data clearing/server change or agent-started playback. App-private data not byte-compared.

Dev14 is now installed experimental, superseding candidate NOT INSTALLED below. Install/startup verified; collection logos/fallback/Back/default-theme and playback regressions remain NOT RUN physically. Dev13 retained prospective rollback, downgrade/data compatibility untested. V8.2 remains latest retained release.

Evidence and targeted before-files: ../../backups/20260912-171534-dev14-install. Local active non-secret checkpoint; no NAS copy or restore-test claim. Next: Gary checks movie logos while browsing inside a collection.
## Dev14 collection-logo candidate — 12 September 2026

Gary confirmed dev13 browsing works well, then reported written movie titles inside a collection, with logos appearing only after opening details. General dev13 browser acceptance is USER-CONFIRMED; exact U04 steps were not enumerated.

Source diagnosis: BOX_SET navigates to CollectionFragment/EnhancedBrowseFragment, while ModernLibraryBrowser was gated only on MOVIES/TVSHOWS collectionType. Dev14 extends the ElegantFin gate to BaseItemKind.BOX_SET, reusing the existing selected-item backdrop/logo/metadata/synopsis preview and text fallback. Collection queries, card titles and click handlers remain unchanged. No new image endpoint, playback or theme behavior was added.

Candidate NOT INSTALLED: [dev14 APK](../../apk-inspection/20260912-dev14-collection-logos/jellyfin-androidtv-v0.0.0-dev.14-debug.apk). org.jellyfin.androidtv.debug / 0.0.0-dev.14 / code 14; 67,548,458 bytes; SHA256 FAFF1E6BDC93F4B4D4FFAC694293FCE3592ACBC144875D9F0E243490686CA108. Certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13, apksigner v1/v2 and package verification pass. All seven PNG category assets included.

Build successful in 5m 20s. 39 fresh app tests pass; five unchanged backend tests UP-TO-DATE with passing XML, not freshly executed. Lint unchanged from dev13: 81 errors, 318 warnings, 422 hints; no new findings, not clean lint. Diff/source checkpoint checks passed. Retained candidate directory contains build log, reports, source-tracked.patch, source-checkpoint and hash manifest. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54, experiment/mobile-inspired-tv-v12 plus preserved dirty work; no commit/reset/branch change.

Dev13 remains installed, retained SHA256 5E6F7D258B53D8F5BC26E74C335675F340F29C1BDE9F8CB0E30F943F5221D51C rechecked before build. V8.2 remains latest retained release. No device/server/media actions this pass. Dev14 physical collection-logo/fallback/focus/Back/mixed-media and default-theme regressions NOT RUN; installation requires Gary's request. Prospective downgrade/data compatibility untested.

Targeted before-files: ../../backups/20260912-170324-dev14-collection-logos. Local non-secret active checkpoint; no NAS copy or restore-test claim. Restore mirrored files for deliberate source/document rollback, preserving later work. Next: authorized install then dev14 collection preview catalogue checks.
## Dev13 installed — 12 September 2026

Gary authorized installation. Fresh ADB identified Gary's SHIELD Android TV at .69; .58 Echo excluded. Pre-install dev12/code 12 APK hash matched retained CDFD79A269D65347893A9407D9E506796D4EDBB8DBD3DD213A964DA9AD402569. Playback already stopped (state 0, speed 0, position 731055ms); no playback command issued.

In-place adb install -r succeeded. Installed org.jellyfin.androidtv.debug / 0.0.0-dev.13 / code 13, SHA256 5E6F7D258B53D8F5BC26E74C335675F340F29C1BDE9F8CB0E30F943F5221D51C matches the retained candidate. Established candidate certificate SHA256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; update accepted. Startup Status ok, WARM, 1596ms; MainActivity foreground and signed-in Home/category artwork/Continue Watching observed. No uninstall, data clearing, server change or agent-started playback. Private settings/data not byte-compared.

Dev13 now installed experimental, superseding candidate NOT INSTALLED status below. Installation/startup PASS; U04 grid previews, toolbar, D-pad and Back/scroll acceptance, plus theme/player regressions NOT RUN on dev13. Dev12 remains retained prospective rollback; downgrade/app-data compatibility untested. V8.2 remains latest retained release.

Evidence and targeted documentation before-files: ../../backups/20260912-165644-dev13-install. Local non-secret checkpoint, no NAS copy or restore-test claim. Installation record: ../../apk-inspection/20260912-dev13-library-browser/installation.md. Next: Gary's Movies/TV browser check and U04 qualification.
# Current release and artifact evidence

## Dev13 library-browser candidate ready — 12 September 2026

Modernized ElegantFin Movies/TV landing pages and full grids with a shared selected-item backdrop/logo/title/metadata/synopsis preview, larger readable toolbar controls and active-filter styling, poster density/spacing adjustments, and loaded-adapter/selection/scroll retention for normal Back navigation. Sort/filter/letter changes reset selection; metadata uses existing loaded items. Other libraries/default themes and dev12 series/season theme playback paths preserved. No device/server/playback action.

Candidate org.jellyfin.androidtv.debug 0.0.0-dev.13/code 13, 67,548,469 bytes, SHA-256 5E6F7D258B53D8F5BC26E74C335675F340F29C1BDE9F8CB0E30F943F5221D51C, established certificate verified. [Full artifact/source/validation/recovery record](../../apk-inspection/20260912-dev13-library-browser/README.md). Build success 5m 48s; 39 fresh app tests pass, five unchanged backend tests UP-TO-DATE/passing XML. Lint unchanged: 81 errors/318 warnings/422 hints, no new findings. Physical U04 and wider UI/theme/playback regressions NOT RUN; exact Back/scroll restoration not yet confirmed.

NOT INSTALLED; dev12 remains last verified installed and retained prospective rollback, downgrade untested; V8.2 latest retained release. Next: authorized attended installation and U04 grid/preview/tools/Back qualification. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev13-library-browser.md), not assumed applied.

## Dev12 feature acceptance confirmed by Gary — 12 September 2026

Gary replied “Yes they work” after explicit clarification covering Playlists artwork, TV logos and theme music including stopping when leaving. Record these requested feature checks as USER-CONFIRMED PASS on installed dev12. Exact shows/navigation sequences were not specified; this does not establish every U02/U03/A02 regression case or dev12 P03a Resume, HDR/audio/subtitle, missing-theme/network/default-theme tests. No new device operation or build. Prior NOT RUN/PARTIAL entries describe the earlier installation boundary.

Dev12 remains installed experimental, exact identity unchanged in the installation record; dev11 retained rollback (downgrade untested), V8.2 latest retained release. Next: keep dev12 for normal use and address any further feedback; broader regression qualification remains open. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev12-user-acceptance.md), not assumed applied.

## Dev12 installed — 12 September 2026

User-authorized in-place installation succeeded. Fresh Shield/dev11 identity confirmed; active Batman playback cleanly stopped at 6,123,752ms before replacement. Installed org.jellyfin.androidtv.debug 0.0.0-dev.12/code 12 SHA-256 CDFD79A269D65347893A9407D9E506796D4EDBB8DBD3DD213A964DA9AD402569 matches retained candidate. COLD/Status ok signed-in launch passed; Home shows Collections artwork and Batman Continue Watching with 24:14 remaining. [Installation record](../../apk-inspection/20260912-dev12-tv-themes/installation.md).

PARTIAL: Home/identity verified; Playlists, TV logos, audible themes/exit and P03a not tested. Left Home, no agent-started playback or server configuration change. Dev11 retained rollback, downgrade untested; V8.2 latest retained release. Supersedes earlier NOT INSTALLED status. Next: user category/logo/theme checks and remaining attended regression cases. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev12-installed.md), not assumed applied.

## Dev12 TV logos, Playlists and theme music candidate — 12 September 2026

Added missing text-free Playlists artwork, enabled series logo above episode title on Next Up details, added seriesId logo fallback and a non-focusable logo on season browser headings. Added automatic TV theme music for series/seasons, default-on Customization toggle, quiet single-play per visit, normal music preservation, lifecycle/audio-focus stop and stale callback guards. Uses existing server themes without queue/progress reports. Goldbergs/IT Crowd root theme.mp3 observed; The Shards logo exists but no root theme/theme-music directory found. No server/media/device mutation.

Candidate org.jellyfin.androidtv.debug 0.0.0-dev.12/code 12, 67,680,789 bytes, SHA-256 CDFD79A269D65347893A9407D9E506796D4EDBB8DBD3DD213A964DA9AD402569, established certificate verified. [Full source/artifact/validation/recovery record](../../apk-inspection/20260912-dev12-tv-themes/README.md). Final build success 4m 40s; 39 fresh app tests pass, five unchanged backend tests UP-TO-DATE with passing XML. Lint 81 errors/318 warnings/422 hints: two new English-setting MissingTranslation errors and one UseKtx style warning. Physical U02/U03/A02/P03a NOT RUN; no audible theme or TV layout claim.

NOT INSTALLED. Dev11 remains last verified installed and retained prospective rollback, downgrade untested; V8.2 latest retained release. Next: authorized attended installation then category/logo and theme playback/exit checks. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev12-tv-themes.md), not assumed applied.

## Dev11 installed — 12 September 2026

User-authorized in-place installation succeeded after fresh Shield identity, stopped playback and installed dev10 rollback-hash verification. Device org.jellyfin.androidtv.debug 0.0.0-dev.11/code 11 SHA-256 43F280A01C8852A0B8299AFE62253A0E0B057C00B85C1859726BC6C5CAEA6DDC matches candidate. Package-scoped WARM launch returned Status ok; signed-in Home shows the new Collections artwork filling the preview to the top edge. [Installation evidence and limits](../../apk-inspection/20260912-dev11-library-artwork/installation.md).

Acceptance PARTIAL: Home/Collections screenshot verified; five other categories, physical crop/brightness, navigation/default-theme/details and dev11 playback NOT RUN. No agent-started playback or server change; left Home for Gary. Dev10 retained rollback, downgrade untested; V8.2 latest retained release. Supersedes earlier candidate NOT INSTALLED status. Next: category artwork review and remaining U02/U03/P03a qualification. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev11-installed.md), not assumed applied.

## Dev11 library artwork candidate ready — 12 September 2026

Bundled all six approved artwork proposals for ElegantFin library previews/cards. Compact Home shortcuts keep labels; initial null-selection hero uses Collections. Library name/type mapping keeps Music–Staging distinct, excludes individual media and preserves unsupported/default-theme server artwork. No server or device operation.

Candidate org.jellyfin.androidtv.debug 0.0.0-dev.11/code 11, SHA-256 43F280A01C8852A0B8299AFE62253A0E0B057C00B85C1859726BC6C5CAEA6DDC, 65,216,446 bytes. Established certificate verified. [Full identity, source checkpoint, validation and recovery](../../apk-inspection/20260912-dev11-library-artwork/README.md).

35 fresh app tests pass; five unchanged backend tests UP-TO-DATE/passing records. Initial missing-ID test-fixture compile failure corrected; complete retry succeeded in 2m 49s. Lint 79 errors/317 warnings/422 hints: one newly reported existing app_banner XML/PNG duplication, no new errors. All six original-resource hashes and APK inclusion verified. Physical dev11 U02/U03/P03a NOT RUN. NOT INSTALLED; dev10 remains last verified installed, V8.2 latest retained release. Next: authorized attended installation and category/crop/navigation qualification, including still-open dev10 UI acceptance. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev11-library-artwork.md), not assumed applied.

## Dev10 installed — 12 September 2026

Explicitly authorized in-place install succeeded. Device org.jellyfin.androidtv.debug 0.0.0-dev.10/code 10 and APK SHA-256 830051711DAC964CBEE6084191BA6B48811DB5C6CD491601CE67822FACFD7733 match retained candidate. Signed-in cold launch passed. Visual acceptance incomplete: initial library preview blank; later navigation sequence inconclusive. Full modern details and playback not qualified. See ../../apk-inspection/20260912-dev10-artwork-polish/installation.md. Supersedes prior NOT INSTALLED status.

Dev9 retained rollback SHA-256 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D matched installed bytes before update; downgrade/app-data compatibility untested. V8.2 latest retained release; no release promotion.

## Dev10 artwork polish candidate — 12 September 2026

Latest retained experimental candidate, NOT INSTALLED: org.jellyfin.androidtv.debug 0.0.0-dev.10/code 10. APK ../../apk-inspection/20260912-dev10-artwork-polish/jellyfin-androidtv-v0.0.0-dev.10-debug.apk, 51,143,487 bytes, SHA-256 830051711DAC964CBEE6084191BA6B48811DB5C6CD491601CE67822FACFD7733. Certificate SHA-256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; v1/v2 verified, aapt/apksigner exit 0. HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 on experiment/mobile-inspired-tv-v12 plus verified tracked/untracked snapshot in candidate folder. See candidate README for exact build, evidence and limitations.

Build success in 6m 16s; 32 fresh app tests pass, five unchanged backend tests UP-TO-DATE with passing records. Lint 79 errors/316 warnings/422 hints: one removed MissingTranslation error and one new UseKtx style suggestion. Four compiler safe-call warnings documented. Signature/assembly/diff/source comparison pass. Physical dev10 home/details/playback acceptance NOT RUN; no install/device operation/publication.

Dev9 remains last verified installed. Its retained SHA-256 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D rechecked before build; prospective rollback after dev10 with downgrade/app-data compatibility untested. V8.2 remains latest retained release. Historical entries retain dated status.

## Dev9 installed — 12 September 2026

Installed org.jellyfin.androidtv.debug 0.0.0-dev.9/code 9, verified device APK SHA-256 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D matching the candidate below. Explicitly authorized in-place installation succeeded; signed-in cold launch passed. U03 partial: action timestamp/metadata clipping and inconclusive delayed action-focus check remain. Back to Home restored Aliens. Playback not started; physical dev9 Resume and broader acceptance NOT RUN. See ../../apk-inspection/20260912-dev9-modern-details/installation.md. This supersedes the prior NOT INSTALLED designation.

Immediate retained rollback is dev8, SHA-256 7E5378AA0B34A99A9D45E9728C16FDA40636BD5EA7F0CC435DC68A8F05156F66, matched installed bytes before update. Downgrade/app-data compatibility untested. V8.2 remains latest retained release; no release promotion.

## Dev9 details candidate — 12 September 2026

Latest retained experimental candidate: org.jellyfin.androidtv.debug 0.0.0-dev.9/code 9, NOT INSTALLED. APK ../../apk-inspection/20260912-dev9-modern-details/jellyfin-androidtv-v0.0.0-dev.9-debug.apk; 51,127,631 bytes; SHA-256 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D. Certificate SHA-256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; v1/v2 verified. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 on experiment/mobile-inspired-tv-v12 plus complete tracked/untracked source checkpoint in candidate folder. See its README for exact command, toolchain, reports and limits.

Modern ElegantFin movie/series/episode details and landscape episode rows built successfully in 5m 48s. 32 fresh app tests passed; five unchanged backend tests UP-TO-DATE with passing records. Lint 80 errors/315 warnings/422 hints with no new findings versus dev8; signature/assembly/diff checks passed. Dev9 physical U03/P03a acceptance NOT RUN. No device operation or publication in this source/build pass.

Dev8 remains last verified installed. Retained dev8 SHA-256 7E5378AA0B34A99A9D45E9728C16FDA40636BD5EA7F0CC435DC68A8F05156F66 rechecked before building; prospective rollback after dev9, downgrade/app-data compatibility untested. V8.2 remains latest retained release. Historical entries below retain their dated boundaries.

## Dev8 installed - 12 September 2026

Installed experimental build: org.jellyfin.androidtv.debug 0.0.0-dev.8/code 8. Retained APK ../../apk-inspection/20260912-dev8-home-polish/jellyfin-androidtv-v0.0.0-dev.8-debug.apk; 51,127,611 bytes; SHA-256 7E5378AA0B34A99A9D45E9728C16FDA40636BD5EA7F0CC435DC68A8F05156F66. Device-side hash/version verified after successful in-place installation. Certificate SHA-256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; v1/v2 verified. HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 plus exact tracked patch/untracked source retained with APK; see its README for build/toolchain/acceptance evidence.

32 fresh app tests passed, five unchanged backend tests UP-TO-DATE with passing records. Lint reviewed: 80 errors/315 warnings/422 hints, the only new finding is missing translations for the new English library hint. Focused physical home cases passed (shortcut contrast, library preview, row/toolbar navigation, details Back). Aliens Resume from 1:01:15 advanced normally; Gary confirmed correct scene/picture/sound. Playback left running. Broader default-theme/cold-start/error/performance/playback matrix not run. Not promoted to a release.

Immediate rollback is retained dev7 r2, SHA-256 2ED5AC444FFB7257B1776C4F8DC2ECAEA974021B1822635F3C5298E31F0A181A; matched installed bytes before dev8. Downgrade/app-data compatibility untested. V8.2 remains latest retained release. Historical entries below retain their dated boundaries.

## Dev7 installed - 12 September 2026

Final r2 dev7/code 7 installed in place after Gary's explicit approval. Device-side SHA-256 2ED5AC444FFB7257B1776C4F8DC2ECAEA974021B1822635F3C5298E31F0A181A matches the retained candidate below. Signed-in Home loaded and selected Aliens artwork/metadata/progress rendered. U02 PARTIAL: shortcut text contrast issue and focus/empty-preview follow-up; no dev7 playback test started. Details: ../../apk-inspection/20260912-dev7-modern-home-r2/installation.md. This supersedes candidate-stage NOT INSTALLED statements below.

Dev6 retained as immediately prior rollback with matching signature; downgrade/app-data compatibility untested. V8.2 remains latest retained release.

## Dev7 modern home candidate - 12 September 2026

Latest retained experimental candidate is dev7/code 7, NOT INSTALLED, not promoted to release. Final artifact: ../../apk-inspection/20260912-dev7-modern-home-r2/jellyfin-androidtv-v0.0.0-dev.7-debug.apk, 51,187,243 bytes, SHA-256 2ED5AC444FFB7257B1776C4F8DC2ECAEA974021B1822635F3C5298E31F0A181A. Package org.jellyfin.androidtv.debug; certificate SHA-256 d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13; v1/v2 signature verification exit 0. Source HEAD 5abaa15745afe5016f2ae12f88845794ffe5ff54 plus exact tracked patch and untracked-source retained with the APK. See candidate README for branch, toolchain, commands, build timing and manifest.

Native Balanced home implementation passed assembly, 32 fresh app tests and signature checks. Five unchanged backend tests were UP-TO-DATE with passing records (37 total records, no failures/errors/skips). Lint reviewed: 79 existing errors, 315 warnings, 422 hints; no findings in changed home files. Only three new AGP availability warnings versus dev6. First assembled draft retained separately in ../../apk-inspection/20260912-dev7-modern-home is superseded because it preceded the resource-access lint fix; do not install that draft.

Currently installed remains dev6 as verified below. Retained dev6 SHA-256 9AB569CC304B14E2D235399C45908A09234BA7A9836EF800DDC4A3424FC2DB81 matched mutable output before building; it is the prospective rollback after dev7, with downgrade/app-data compatibility untested. V8.2 remains latest retained release. No device operation/install occurred in this source/build pass. Physical U02 and dev7 P03a are NOT RUN; native visual/focus/performance acceptance remains open.

## Dev6 installed and Aliens resume accepted - 12 September 2026

Gary explicitly approved installation after advancing Aliens to about 30 minutes. The verified Shield was playing Aliens (1986), Special Edition, 2160p/TrueHD Atmos. Agent paused and exited playback gracefully at 1,899,387ms (31:39.387), then installed dev6 with adb install -r: Success. No uninstall or data clearing. Installed package org.jellyfin.androidtv.debug, version 0.0.0-dev.6/code 6, device-side APK SHA-256 9AB569CC304B14E2D235399C45908A09234BA7A9836EF800DDC4A3424FC2DB81, matching the verified retained candidate. Certificate remains d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13.

Reopened with the package-scoped jellyfin://launch intent; signed-in Home and Continue watching loaded without a login prompt. Preferences XML and legacy authentication XML hashes matched pre-install. authentication_store.json hash changed during the transition; its contents were not inspected or retained, so no claim of byte-identical private app data. Functional signed-in access and preferences preservation passed.

Fresh Aliens details displayed Resume from 31:39. Agent activated that focused Resume button. First MediaSession readback: PLAYING at 1,899,393ms; second readback: PLAYING at 1,917,174ms, speed 1.0, error=null. Gary confirmed: Yes, correct scene and normal picture/sound. P03a Aliens Resume case PASS for this installation/session. Playback was left running after confirmation. Pre-install details lacked a Resume button until returning through refreshed Home/details; subsequent server-backed Continue watching/details showed the captured position. No direct server progress rewrite was performed.

Dev6 is now the installed experimental build; V8.2 remains the latest retained release. Dev5 is retained as the immediately prior, verified matching-signature rollback candidate (downgrade not tested). Build qualification remains 34 tests passed, signature/assembly passed, lint 79 errors/312 warnings/422 hints with no findings in resume-change files. Other movies, cold-start/repeated-resume, Play-from-beginning, next-item and full HDR/audio/control matrix remain NOT RUN physically; one attended success does not establish complete release qualification.

Evidence: ../../apk-inspection/20260912-dev6-resume/installation.md and install-checkpoint-path.txt; the linked dated backups checkpoint contains install output, installed-version readback, private-file hashes (no secrets), Resume label and both playback position readbacks. Source/APK provenance remains in the candidate README and CURRENT-RELEASE.md. Shared overview submission: ../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev6-installed-resume-pass.md (pending, not assumed applied).

Next: continue normal use of dev6 and record any recurrence with title, displayed Resume timestamp and actual start position. Broader physical cases remain open; do not interrupt Gary's successful playback for additional tests without a new attended boundary.


## Pre-install verification - 12 September 2026

Read-only ADB verified SHIELD Android TV / Android 11 with org.jellyfin.androidtv.debug 0.0.0-dev.5, code 5. Device-side SHA-256 of installed base.apk: `1349E8E0B6769881E5379F03A29EF86A278A61D736363A67D8C0C5A5535AD667`, identical to the retained `../../apk-inspection/20260909-dev5-migration/jellyfin-androidtv-v0.0.0-dev.5-debug.apk`. This establishes the installed artifact identity now; it does not repair missing historical dev5 dirty-source provenance or establish physical acceptance. Read-only preferences confirm the rewritten video player is enabled. No app settings or playback were changed.

A dev6/code 6 resume-fix candidate has been built and automatically checked from HEAD `5abaa15745afe5016f2ae12f88845794ffe5ff54` plus preserved existing theme/trailer changes and the new resume changes. Candidate evidence directory: `../../apk-inspection/20260912-dev6-resume`. Verification completed: 34 tests passed, debug assembly/signature passed; lint has 79 errors/312 warnings/422 hints, with zero findings in resume change files. This is an uninstalled candidate, not a qualified release. No install/publication authorization inferred from debugging approval. Dev5 remains installed and V8.2 remains the latest retained release.

Workspace relocation (9 September 2026): this checkout is now under `J:\My Drive\Jellyfin apk\source`. Existing relative release paths remain valid. A byte-identical dev5 evidence copy is retained at `../apk-inspection/20260909-dev5-migration/jellyfin-androidtv-v0.0.0-dev.5-debug.apk`, in addition to the original mutable build output. This is preservation, not release promotion or new device acceptance. Migration verification is recorded in `../../00_Project/migration`.

Evidence reviewed: 2026-09-09. Paths below are relative to `source` unless stated otherwise. This is a local evidence audit, not a new build, installation or live Shield test.

## Current designation

- **Latest retained release: Shield Enhanced V8.2.** Supported by `shield-enhanced-v8.2` tag, retained release folder/APK/checksum and the experiment document explicitly retaining V8.2 as its baseline. This is an unofficial debug release, not a claim of public publication or comprehensive device qualification.
- **Latest retained experimental build: `0.0.0-dev.10`, version code 10.** Retained in ../../apk-inspection/20260912-dev10-artwork-polish with exact source checkpoint, signature and test evidence; installed, visual acceptance incomplete, not release-promoted.
- **Currently installed Shield build: `0.0.0-dev.10 debug`, code 10, installed and device-hash verified on 2026-09-12.** Signed-in launch observed; initial preview blank and visual/navigation acceptance incomplete. Dev10 playback acceptance not run.
- **Latest documented release device evidence:** the shared homelab overview records V8.2 installed, launch from Google TV Home, Batman remote playback with control enabled, and the corrected launcher banner. V8 notes independently record successful in-place install and foreground registration. No complete dev5 playback acceptance record was found; Gary's installation confirmation is not a full test pass.

## Artifact identity verified from APKs

All six APKs below were inspected on 2026-09-09 with retained Android build-tools 36.0.0 `aapt dump badging` and `apksigner verify --verbose --print-certs`; SHA-256 was recomputed with PowerShell `Get-FileHash`.

Shared identity: package `org.jellyfin.androidtv.debug`; minimum API 23; target/compile API 36. All verified with v1 and v2 signatures; v3/v3.1/v4 reported false. Certificate DN: `C=US, O=Android, CN=Android Debug`. Certificate SHA-256:

`d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`

| Artifact | Version name / code | Bytes | SHA-256 |
|---|---|---:|---|
| RC1 | 0.0.0-dev.1 / 1 | 54397007 | `ADC6486759975111ADDA78AD905B39E37BDAFBE6792788EEE9DF4E0DFC90FFEE` |
| V8 | 0.0.0-dev.1 / 1 | 54397007 | `8D5D7068860B84024950C5A8BC0F7F2DD56E1DE6F2EBEFB2FE16A8F371DFE826` |
| V8.1 | 0.0.0-dev.1 / 1 | 54397007 | `20C96F0108AD322EF07216F1FF8AC460118B9E5187E1E728E4855B1AF5862B1A` |
| V8.2 | 0.0.0-dev.2 / 2 | 51092188 | `AADAAF46599D5A8F502540BA7646D10F4CD87A77BD1398CB0EF1F90697F900C2` |
| Retained dev4 | 0.0.0-dev.4 / 4 | 51094333 | `4DC7DEC19B402A46B38868F1AE6997E4569F41F76707ED40AC3FEF2642372BA3` |
| Experimental dev5 | 0.0.0-dev.5 / 5 | 51110695 | `1349E8E0B6769881E5379F03A29EF86A278A61D736363A67D8C0C5A5535AD667` |

Exact paths:

- RC1: `../release/shield-enhanced-v0.1-rc1/Jellyfin-AndroidTV-Shield-Enhanced-v0.1-rc1-debug.apk`
- V8: `../release/shield-enhanced-v8/Jellyfin-AndroidTV-Shield-Enhanced-V8-debug.apk`
- V8.1: `../release/shield-enhanced-v8.1/Jellyfin-AndroidTV-Shield-Enhanced-V8.1-debug.apk`
- V8.2: `../release/shield-enhanced-v8.2/Jellyfin-AndroidTV-Shield-Enhanced-V8.2-debug.apk`
- Dev4: `../apk-inspection/shield-installed-jellyfin-debug-v0.0.0-dev.4.apk`
- Dev5: `app/build/outputs/apk/debug/jellyfin-androidtv-v0.0.0-dev.5-debug.apk`

V8 and V8.2 recomputed hashes match their retained checksum files. V8.1 has no adjacent release notes/checksum in the inspected folder; its hash above is newly measured. Marketing release labels V8/V8.1 are not Android version names and cannot distinguish those APKs without hashes.

## Source and dates

| Item | Supported evidence and limitation |
|---|---|
| V8.2 source ref | Tag `shield-enhanced-v8.2` and branch `release/shield-enhanced-v8` point to `7c685b6e569570483007ddc807b1af1fd231a49d` (full Jellyfin wordmark). Source commit dated 2026-09-05 17:27:11 +01:00. Tag association is not a reproducible-build attestation. |
| V8 source ref | Tag `shield-enhanced-v8` points to `e6de1bdfb5969e9840acf84c5142a9eaff43e1c4` (remote capability on resume). |
| V8.1 source | No V8.1 tag found. Intervening commit `ad1a934c4bb54d3f79d87d785426f210af0ae123` adds `jellyfin://launch`; association with the named APK is plausible but unverified. |
| Current source | Branch `experiment/mobile-inspired-tv-v12`, HEAD `5abaa15745afe5016f2ae12f88845794ffe5ff54`; same HEAD as `experiment/embedded-trailers-elegantfin`. Five modified tracked files and three untracked source/test files predate this documentation task; see HANDOFF. |
| Dev5 source correspondence | Existing output metadata agrees with APK version/code. No immutable dirty-source snapshot or full build provenance was found tying this APK exactly to today's eight source/test changes. Do not attribute dev5 solely to clean HEAD. |
| Build dates | Exact build start/end times unknown. File modification times in UTC: V8 `2026-09-05T15:57:28Z`; V8.1 `2026-09-05T16:14:59Z`; V8.2 `2026-09-05T16:25:22Z`; dev5 `2026-09-08T22:12:53Z`. These support approximate artifact chronology only. Dev4 retained file mtime `2026-09-08T22:18:11Z` is not its build time. |

## Verification and conflicts

- Existing `app/build/test-results/testDebugUnitTest/TEST-*.xml` records 25 tests across five suites, zero failures/errors/skips, dated 2026-09-08 22:12:55–57 UTC: CustomSeekProvider 10, TrailerMediaKey 3, VideoSpeedController 8, TrailerUtils 2, TimeUtils 2. These were read, not rerun. Their proximity to dev5 output is suggestive, not proof of exact APK/source correspondence.
- Existing `app/build/reports/lint-results-debug.txt` is dated 2026-09-05 and reports **74 errors, 315 warnings, 422 hints**. `app/build.gradle.kts` sets `abortOnError = false`. V8 notes say lint passed, but that does not establish a clean lint report; the retained report is also too old to qualify the dirty dev5 work. Do not claim these findings are all new or all regressions without comparing a baseline.
- V8 notes report compile, tests, lint task, assembly, signatures, installed-certificate match, in-place installation, foreground registration and Batman remote playback passing historically. Those statements were not revalidated on a device in this audit.
- Shared `backups/20260905-shield-jellyfin-v8.2-banner/README.md` records V8.2 build/signature checks, in-place install with app data preserved, banner screenshot, HA launch and media-control capability on September 5. That specific check ended at Home without playback; broader playback evidence comes from the overview. Its pre-publication statement is superseded by the overview's later publication record.
- `docs/shield-enhanced.md` still names RC1 as its installation example. `../BUILD-INFO.md` describes the August 29 PR5743 APK, commit `df90e9814cec5ce3a5a8029b75f3c7fc571ea76b`, not the current release. Its Profile 8 test differs from the Profile 7 test instructions; preserve both as distinct historical scenarios.
- The initial experiment build command uses dev3. Codes 4 and 5 now exist, so that command is not a safe next-upgrade version selection.
- Gary confirms installed app version dev5. Actual Shield model/firmware, display/audio chain and full current device test status were not independently checked. The shared homelab overview records server 12.0.0 on September 8; this is dated documentation, not a fresh server query.
- The shared overview records RC1 and V8.2 publicly published, with V8.2 asset hash matching the retained APK and anonymous download checks passing historically. This supersedes the old RC1 checklist's 'nothing published' statement. Current remote availability was not queried. Recorded V8.2 page: https://github.com/Smotheredhope80/jellyfin-androidtv/releases/tag/shield-enhanced-v8.2 .

## Rollback resources and boundary

Retain V8.2 as the pre-trailer/theme experimental baseline, and retain dev4 as a separate inspected artifact whose current usability is unverified. V8 and the parent `Jellyfin-AndroidTV-Shield-V7.6.3-Raised-Chapter-Panel-Text-Fix-debug.apk` provide older fallback candidates. `../backups/V7.6.3-source-checkpoint-2026-08-31` is a source checkpoint, not an Android app-data backup.

All six inspected APKs share a certificate. Nevertheless, going from dev5/code 5 or dev4/code 4 to V8.2/code 2 is a downgrade; plain `adb install -r` must not be promised to succeed. During authorized rollback, verify the installed package/version and saved APK checksum first. For a compatible debuggable package, `adb -s <verified-device> install -r -d <retained-apk>` is a possible downgrade attempt, not a tested guarantee. If Android rejects it, stop and assess; do not automatically uninstall or clear data. Uninstalling the debug package loses its private settings/login data. The official `org.jellyfin.androidtv` app remains a separate alternative. No downgrade or app-data restoration was exercised in this audit.

Primary local references: `../release/shield-enhanced-v8/RELEASE_NOTES.md`, retained APKs/checksums, Git refs/history, `EXPERIMENT-EMBEDDED-TRAILERS-ELEGANTFIN.md`, build output metadata and reports; shared `J:\My Drive\Garro Homelab project\# Gary's Home Lab Overview.txt` entries around lines 1514–1518 (RC1), 1591–1599 (V8.2), 2152–2157 (server migration). Gary's 2026-09-09 confirmation establishes the reported installed version. Toolchain details and device acceptance cases are in `SHIELD-TEST-CATALOG.md`.


## Dev6 candidate verification - 12 September 2026

Candidate: jellyfin-androidtv-v0.0.0-dev.6-debug.apk; 51,110,718 bytes.
Package org.jellyfin.androidtv.debug; version 0.0.0-dev.6/code 6; minSdk 23/targetSdk 36.
APK SHA-256: 9AB569CC304B14E2D235399C45908A09234BA7A9836EF800DDC4A3424FC2DB81.
Certificate SHA-256: d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13. apksigner v1/v2 verification PASS; matches established debug identity. apksigner prints META-INF v1 metadata warnings; v2 verification passes. Full output is ../../apk-inspection/20260912-dev6-resume/signature.txt.

Using retained JDK 21/SDK/Gradle wrapper via 00_Project/Invoke-Gradle.ps1, final command: :app:testDebugUnitTest :playback:media3:exoplayer:testDebugUnitTest :app:lintDebug :app:assembleDebug -Pjellyfin.version=0.0.0-dev.6 --offline --no-daemon --no-configuration-cache. Final run succeeded in 3m 4s, exit 0, at about 11:43 Europe/London. The preceding 17m 13s build assembled the APK but failed test-fixture compilation (missing BaseItemDto.type); corrected test fixture passed on rerun. Main APK remained byte-identical on final assembly. Exact logs are ../../backups/20260912-111957-resume-investigation/build-dev6.log and verify-dev6.log. APK packaging was observed during this session around 11:39; no timestamp inferred solely from file mtime.

34 tests passed, 0 failures/errors/skips: app 29 (including 4 new queue tests) and backend 5 new selection tests. XML evidence is retained in app-tests and backend-tests. git diff --check PASS.

Fresh lint: 79 errors, 312 warnings, 422 hints. Older September 5 report: 74 errors, 315 warnings, 422 hints. Normalizing relocated paths and line numbers, the five additional errors are RestrictedApi findings in pre-existing EmbeddedTrailerActivity.dispatchKeyEvent code (lines 88/90/94), not edited in this task; the three removed warnings are AndroidGradlePluginVersion. Other error keys/counts match the old report. Zero findings reference the resume change files. This is not a clean-lint or fully qualified release; inherited issues remain. Full fresh text/XML reports are retained here; baseline is in the targeted checkpoint.

Installed dev5 hash matches the preserved ../20260909-dev5-migration APK. Dev5 remains installed; V8.2 remains the latest retained release. No installation, publication, service/settings mutation or agent-triggered playback. Gary prepared Aliens at approximately four minutes for attended P03a; all physical resume/HDR/audio/control acceptance is NOT RUN.

Next: scoped approval for in-place dev6 installation on the verified Shield, preserving app data/signing, after playback is stopped; then attended Resume versus displayed timestamp and Play-from-beginning checks. In-place install will close the app and interrupt active playback. Retained dev5 provides a matching-signature downgrade candidate but downgrade acceptance/app-data recovery is not tested; do not uninstall/clear data on rejection.
## Dev29 installed experimental on Gary's Shield — 27 September 2026

`org.jellyfin.androidtv.debug` / `0.0.0-dev.29` / code 29 was installed in place on Gary's Shield `192.168.68.69:5555` at 10:00 BST. Retained APK: `apk-inspection/20260927-dev29-safe-details-youtube-focus/jellyfin-androidtv-v0.0.0-dev.29-debug.apk`, 64,276,435 bytes, SHA256 `3DF3BDC706A6819DB942DC6F1F9A282FD39E803149348B0DC3832D24076D5A2F`; installed APK readback matched. `apksigner` v1/v2 verification passed; debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Source: branch `experiment/mobile-inspired-tv-v12`, base HEAD `209169cd1d501f1b8989d4f105b736a4bda6d71b` plus three modified app files in [source checkpoint](../../apk-inspection/20260927-dev29-safe-details-youtube-focus/source-checkpoint/) with hash manifest. Unrelated `AGENTS.md` edit excluded. No source commit or push for dev29 yet.

Offline `:app:testDebugUnitTest :app:lintDebug :app:assembleDebug` passed, 45 tests, zero failures/errors/skips. Lint: 81 errors, 326 warnings, 423 hints; five new unused deletion-string warnings, no new code finding. Shield startup and populated home passed; movie detail showed no Delete button. More action remains unverified; Gary reports trailer remote navigation remains tab-like after player focus, so the focus change did not resolve that experience. Full U10 and playback acceptance remain unrecorded. The movie playback screen was briefly entered during navigation and exited with Back, without a playback verdict. [Build/install evidence and limitations](../../apk-inspection/20260927-dev29-safe-details-youtube-focus/README.md). V8.2 remains the retained release; Cindy's Shield untouched. Dev28 rollback APK SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E` is retained; no rollback was exercised.
