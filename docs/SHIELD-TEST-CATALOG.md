# Shield verification catalog

Workspace relocation: use `J:\My Drive\Jellyfin apk\source` as the Gradle root. The relative retained toolchain paths below remain valid. `../../00_Project/Invoke-Gradle.ps1` selects the workspace-local JDK, SDK and Gradle cache; `../../00_Project/BUILD-ENVIRONMENT.md` records environment and signing dependencies. Migration-readiness checks are not new passes of the physical cases below.

Established from local source and retained instructions on 2026-09-09. These are repeatable procedures, not a list of newly passed tests. Paths are relative to `source`. Historical results are summarized in `CURRENT-RELEASE.md`; all physical cases below are **not run in this documentation audit**.

## Record for every run

Record date/time/timezone, tester, APK SHA-256, package/version/code/certificate, source commit plus dirty state, exact Shield model/firmware, TV/display mode, audio chain, server version, selected media/track characteristics and settings. For each case record PASS / FAIL / BLOCKED / NOT RUN, actual result and a log/screenshot or physical observation. Use representative test media already available; avoid private identifiers in published records.

Before device changes, confirm the target and installation authorization, protect active playback, retain the installed/rollback APK and settings information, and inspect version/signing compatibility. Never use an unverified device address from historical notes. Playback method, actual TV HDR indication and audible results must be observed independently.

## B01 — Build, unit tests and lint

Evidence: Gradle wrapper/properties, `gradle/libs.versions.toml`, `app/build.gradle.kts`, retained SDK/JDK, experiment instructions and existing XML reports.

1. Record Git status/diff and preserve the prior build APK outside mutable build output under a unique name before rebuilding.
2. Use JDK 21 and the local SDK. Retained paths inspected: `../toolchain/jdk/jdk-21.0.12.1+1`, `../toolchain/android-sdk`, `../toolchain/gradle-home`; a second JDK exists under `../toolchains/jdk21`. `local.properties` points to the first SDK. Wrapper is Gradle 9.5.1; catalog specifies AGP 9.2.1, Media3 1.10.1, compile/target 36 and min 23.
3. In PowerShell from `source`, use the command pattern below after choosing a version code above the actual installed/candidate code:

```powershell
$env:JAVA_HOME = (Resolve-Path '..\toolchain\jdk\jdk-21.0.12.1+1').Path
$env:ANDROID_HOME = (Resolve-Path '..\toolchain\android-sdk').Path
$env:GRADLE_USER_HOME = (Resolve-Path '..\toolchain\gradle-home').Path
# Replace the placeholder before running; dev3 from the old experiment is stale.
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug '-Pjellyfin.version=<chosen-version>'
```

4. Record exit status and examine `app/build/test-results/testDebugUnitTest`, `app/build/reports/lint-results-debug.*` and `app/build/outputs/apk/debug/output-metadata.json`. Inspect/hash/sign the actual APK as in B02.

Expected: relevant tests pass, APK assembles, metadata matches intended identity, and lint findings are reviewed against the baseline with no unexplained regressions. `abortOnError=false` prevents using task success as a clean-lint claim. Retained reports show 25 passing tests but an older 74-error lint report; fresh results are required for a new release.

## B02 — APK identity and upgrade

Evidence: `app/build.gradle.kts`, measured release APKs and V8 installation notes.

1. Run `Get-FileHash -Algorithm SHA256 <apk>`, retained build-tools `aapt.exe dump badging <apk>` and `apksigner.bat verify --verbose --print-certs <apk>` with JDK configured. Record tool exit status, package, version/code, APIs and certificate.
2. Compare with intended artifact and installed debug app. Only during authorized install, run `adb -s <verified-device> install -r <apk>` when version/signing permit.
3. Launch, verify version, retained login/settings, orange debug identity, and that the official app remains separately installed.

Expected: verified artifact and intended package update, preserved settings for a compatible in-place upgrade, successful launch. Record rejection as failure/blocker; do not auto-uninstall. V8 has a historical pass, and the shared September 5 V8.2 banner backup README also records successful in-place install with settings retained. Gary confirms dev5 installed on September 9; this audit did not exercise its upgrade path.

## P01 — Dolby Vision Profile 7 / HDR10 base layer

Evidence: parent `SHIELD-TEST-INSTRUCTIONS.txt`, `docs/shield-enhanced.md`, `preference/constant/HdrFormat.kt` under the app Java package.

1. Use Possession (1981), Backrooms and Top Gun: Maverick from the historical instructions if available; record actual stream metadata rather than assuming every copy matches. Possession was documented as HEVC Main 10 DV7.6 with an enhancement layer and HDR10 base layer.
2. Start with subtitles Off. Record Auto behavior first. For the historical override comparison, set Settings > Playback > Advanced > HDR formats > Dolby Vision Profile 7 to Enable; leave other formats Auto. Record that change separately.
3. Record Playback information: play method, video range/codec, audio codec and transcode reason; check server activity for an FFmpeg video-transcode job. Observe TV HDR/DV mode, colors, audible sync, seek recovery and stability.

Expected for the compatible Shield test path: video avoids range-rejection transcoding and plays with correct HDR/DV output or supported HDR10 base-layer handling, stable seeking and sync. Do not require Dolby Vision output for every Profile 7 fallback. Audio transcode is a separate result; subtitle burn-in can independently cause video transcode. Restore Auto if decoding/colors fail. An override cannot add decoder capability.

## P02 — Original Profile 8 override regression

Evidence: historical parent `BUILD-INFO.md` (August 29).

Use the documented A Nightmare on Elm Street Part 2: Freddy's Revenge copy, first verifying it is the Profile 8 / `DOVIWithHDR10Plus` case. Set Profile 8 Enable with other overrides Auto; start without subtitles and repeat P01 observations. Expected from the original test objective: Direct Play instead of `VideoRangeTypeNotSupported`, TV Dolby Vision indication and EAC3 Atmos availability. This is a separate scenario from P01; the old instructions do not establish a pass on today's APK. Restore prior settings afterward.

## P03 — Playback overlay, pause, seek and resume

Evidence: `docs/shield-enhanced.md`; app `ui/playback/CustomPlaybackOverlayFragmentHelper.kt`; CustomSeekProvider unit suite.

Play representative SDR and HDR content, pause, inspect title artwork and elapsed/remaining timing, navigate controls, seek forwards/backwards, resume, then leave/reopen the item and use resume playback. Expected: readable artwork/timing, focus stays reachable, selected seek position is used, audio/video resume in sync and saved playback progress is reflected. The reopened-progress check is a release acceptance extension, not a documented historical pass. Record actual positions and tolerances rather than assuming precise timing.


### P03a - Resume with the experimental video player (12 September 2026)

Record whether Settings selects the experimental/new video player; the dev5 Shield preference was verified enabled on 12 September. With attended playback authorization, record the movie and displayed Resume timestamp before starting. Use Aliens (user-reported failing movie), then another movie. Choose Resume and compare the first stable player position and actual scene with the displayed position, allowing configured preroll and elapsed startup time. Repeat after leaving/reopening the movie, after playing a different movie, and with a cold app launch. Separately choose Play from beginning and verify zero, then ordinary pause/unpause and seek. For queued items confirm the next item does not inherit the first item's resume position. Preserve/record original progress and do not conduct progress-changing tests unattended. Include the P01/P04/P05 output, audio/subtitle and control checks for a playback release. Unit tests are not physical acceptance.

## P04 — Audio and subtitle selection

Evidence: Shield highlights and carried-forward V8 notes; app subtitle settings and playback controls.

Use media with multiple named audio tracks and text subtitles. Select alternate audio, then return to the desired English track where present. Select text subtitles and change appearance/position; reopen playback to check the applied style. Separately test bitmap/PGS subtitles and record any burn-in/transcode.

Expected: human-readable labels, correct audible track, controls retain focus, text subtitle appearance changes apply. Do not expect text styling to recolor bitmap subtitles or treat a PGS-triggered transcode as HDR failure.

## P05 — Chapter browser and speed

Evidence: Shield highlights, V8 carried-forward notes, VideoSpeedController tests and CustomSeekProvider tests.

Open chapters on a title with at least six chapters. Navigate across the five-across filmstrip including its edges, inspect thumbnails/names/timestamps and select a noninitial chapter. Open playback speed, choose an available nondefault speed, then restore normal speed.

Expected: visible focus border and readable labels over translucent backing, appropriate thumbnail proportions, reachable chapter choices, playback seeks to the selected chapter, speed selection changes playback and normal speed restores. Record media-specific missing thumbnails separately.

## R01 — Foreground remote-control registration and launch

Evidence: `../release/shield-enhanced-v8/RELEASE_NOTES.md`, app `data/eventhandling/SocketHandler.kt`, `AndroidManifest.xml`, commits e6de1bdfb and ad1a934c4.

During authorized device/integration testing, launch the debug app using the package-scoped `jellyfin://launch` intent, background and foreground it while signed in, and inspect logs/server session capability. Send remote playback to the verified unique Shield session with an agreed test title (historical example: Batman, 1989). In a controlled network-failure test, observe bounded retries and recovery without disrupting other services.

Expected: foreground return registers media-control capability without reopening startup manually; server reports remote-control support and the intended Shield plays the requested title. Retry handling is bounded (four attempts per V8 notes/source) and terminal failures are logged. Distinguish activity resume from saved media resume (P03). Historical V8 notes report registration/playback passing; launch-link and failure-path behavior require their own results.

## U01 — Theme, home preview and D-pad focus

Evidence: initial experiment document and current dirty `ui/home/HomeFragment.kt`, `HomeRowsFragment.kt`, `ElegantFinHomePreview.kt`, `HomePreviewViewModel.kt`, `ui/presentation/CardPresenter.kt`, `ui/background/AppBackground.kt`.

Select ElegantFin inspired (experimental), cold-launch, browse several rows and move rapidly left/right. Inspect the selected-item title/metadata/overview and missing-metadata cases. Move Up within lower rows and from the first row to the toolbar, then back to rows. Open an item and Back out. Repeat under a default theme.

Expected for the dev6 baseline: native navy/violet styling, legible card focus, a fixed 148dp preview reflecting selected item, no preview focus stop/autoplay, no focus loss, toolbar exit only from the first row, and normal default-theme behavior. Dev7 extends this layout using U02 below. Preview selection uses existing item data; avoid adding per-selection metadata requests. Cold start must not crash on a drawable with no intrinsic size. Device screenshots alone cannot prove the full sequence.

## U02 — Balanced modern home

Dev11 custom artwork qualification (NOT RUN physically): in ElegantFin, select Collections, Movies, TV Shows, Music, Music Videos and Music–Staging; verify the matching local image fills the hero without baked-in category lettering. Staging must remain distinct from Music despite the shared music collection type. Check initial Home before selection (Collections image), quick left/right changes, return from details, and 3:1-to-screen crop/brightness. Compact library shortcuts retain labels and focus behavior. Other ElegantFin library cards use local artwork; individual movies, box sets, albums and unsupported libraries retain normal artwork. Compare a default theme to confirm its server artwork remains. No server image replacement is involved. Repeat the existing U02 navigation and P03a checks after installation.

Dev8 focused run, 12 September 2026: shortcut contrast/library preview and controlled library/movie/toolbar navigation plus details/Back restoration passed on Shield; Aliens Resume passed with Gary confirming scene/picture/sound. Exact APK and per-step evidence: ../../apk-inspection/20260912-dev8-home-polish/README.md. Broader cases below are not covered by that focused pass.

With ElegantFin selected, cold-start Home and traverse Continue watching, library shortcuts, Next up and later configured rows. Verify the selected movie's backdrop/title logo and summary; test a title with no logo/backdrop, an episode inheriting series artwork, and rapid left/right repeats. Check that failed artwork falls back to text without showing the preceding movie's logo. The preview must never take focus or autoplay. Check first-row Up to toolbar, lower-row Up to previous row, Search, Settings, library launch and Back to the original card. Check typography and focused borders at the Shield's actual display scale.

Compare saved Resume and remaining-time labels to fresh details. Test after returning from playback and after a server progress refresh. Missing, invalid or completed progress must not advertise a resume timestamp. Retest P03a for the accepted player path. Repeat navigation in a default theme: original card sizing and home behavior must remain.

Build/unit checks do not establish U02. Record each physical case against its installed APK; the focused dev8 pass above does not cover the remaining matrix. Candidate testing requires an attended installation/playback boundary.

## T01 — Trailer routing and Back

Evidence: `EXPERIMENT-EMBEDDED-TRAILERS-ELEGANTFIN.md`, app `util/sdk/TrailerUtils.kt`, `ui/playback/EmbeddedTrailerActivity.kt`, TrailerUtils tests.

Try local trailers, supported YouTube watch/short/embed/live/Kodi-style URLs and a supported non-YouTube remote provider. Open and exit each with Back; repeat after navigating to another item.

Expected: local trailer uses native player; recognized YouTube opens full-screen embedded player; other remote provider retains external-app routing where a handler exists. Standard YouTube controls/attribution remain visible. Back returns to Jellyfin with usable focus and no lingering audio. Test absent trailer/handler cases without treating missing media as a playback regression.

## T02 — Trailer media keys and lifecycle

Evidence: dirty `EmbeddedTrailerActivity.kt` and `TrailerMediaKeyTests.kt`; retained three-test passing XML.

While a YouTube trailer plays, send PAUSE twice, PLAY twice, PLAY_PAUSE, rewind and fast-forward; repeat near beginning/end and hold a transport key. Navigate controls with D-pad/Center, then background/foreground and Back out.

Expected: PAUSE stays paused, PLAY stays playing, toggle changes state, seeks use bounded ten-second steps, held-key repeats do not double-toggle, and D-pad/Center remain available to WebView controls. Background pauses playback, returning is usable, and teardown stops audio. Unit tests cover command mapping only, not physical WebView behavior.

## T03 — Embed denial and external fallback

Evidence: experiment document and EmbeddedTrailerActivity fallback implementation.

Use a known embed-denied YouTube item (or a controlled error-path fixture), observe errors 101/150 and the resulting external URL; return to Jellyfin. Also test unavailable WebView/external handler where practical.

Expected: denied embedding falls back to the normal YouTube URL without a crash or abandoned audio. Preserve standard player behavior and attribution. Record network/provider restrictions and missing-handler cases explicitly; do not claim successful embedded playback when only fallback was tested.

## X01 — Rollback qualification

Evidence: `CURRENT-RELEASE.md` artifact inventory, historical V8 and PR5743 rollback instructions.

1. During authorized rollback work, inspect installed version/code/certificate and verify a retained rollback hash. Record settings/login preservation requirements; source checkpoints are not device-data backups.
2. Check the downgrade boundary. V8.2 is code 2; dev4/dev5 are codes 4/5. Use `install -r` only where permitted; `install -r -d` may be appropriate for a debuggable downgrade but must be qualified on the actual target.
3. After successful rollback, verify version, login/settings and representative P01/P03/U01 behavior. If rejected, stop before uninstall/data clearing. The separate official app is an alternative; uninstalling debug loses its private data.

Expected: intended prior APK runs and required settings/playback survive, or the result is explicitly BLOCKED with the rejection preserved. No rollback was executed in this audit.
## U03 — Modern movie and TV details

Dev12 TV branding (NOT RUN physically): open Next Up episodes and season folders for The Goldbergs, The IT Crowd and The Shards. Episode details must show the series logo above its specific title; season browser must show a non-focusable series logo alongside the selected episode title. Test missing inherited tags with a valid seriesId, a series without a logo, a failed logo response, rapid selection and Back. Home Next Up logo uses the same series fallback. Text must remain on absent/failed images; check long titles, focus and a default theme. Playlists library must use the seventh local artwork with no baked-in white heading, while individual playlists retain server artwork.

Dev9 installed run, 12 September 2026: PARTIAL. Aliens details renders, but Resume timestamp and DIRECTED BY labels clip. Initial Resume focus recorded; later action sequence found focus in More like this and is inconclusive. Back restored Aliens on Home. Playback not started. See ../../apk-inspection/20260912-dev9-modern-details/installation.md. The candidate-stage NOT RUN note below describes the earlier source/build pass.

Status: NOT RUN physically. Applies only to ElegantFin movie, series and episode details. Default themes and non-video detail types retain their existing presentation. Seasons remain in the existing season browser; Next Up, Upcoming and Next Episode rows use landscape cards and saved remaining time where present.

During a separately authorized attended install/test, verify the exact candidate and stopped/saved playback boundary first. Inspect a movie with Resume, one without progress, a series with Next Up, and an episode. Check a long two-line title, long synopsis expansion/Back, missing artwork, no trailer, multiple versions, favorite/watched state and More overflow. The primary action row should fit, every visible label should be readable, and focused buttons should have white surfaces with dark text/icons. Navigate left/right through actions, Down to season/episode/cast rows, Up back to the previously focused action, open an episode and Back to its row, then Back to Home. Confirm Trailer stays reachable and does not autoplay. Season selection must still open its existing browser.

Check localized labels and larger font settings for clipping; compare a default theme and a non-video detail such as a person to confirm original layout behavior. Repeat foreground return and a fresh details launch. During attended playback testing, repeat P03a for Resume versus Play from beginning, series Next Up and Trailer routing; do not infer these passes from unchanged handlers or unit tests. The initial dev9 source/build stage does not install, change progress or interrupt playback.
### Dev10 artwork and details refinement acceptance

Installed run 12 September: PARTIAL. Signed-in Home loaded and duplicate preview text is absent, but initial artwork area blank. Later navigation reached a collection browser; controlled sequence inconclusive. FullDetails/playback acceptance unverified. See ../../apk-inspection/20260912-dev10-artwork-polish/installation.md. The NOT RUN statement below records the earlier candidate stage.

Status: NOT RUN physically. After an authorized attended dev10 installation, verify U02 artwork extends behind the toolbar to the top edge, stays top-aligned on portrait-heavy scenes, and keeps the same first-row-only Up boundary. On My media selection verify no duplicated library title/hint or left-side gradient; library shortcuts and the row header remain. Check several libraries, missing artwork and fast focus changes, then a movie preview where the text readability gradient is still expected.

For U03, a movie/series logo should replace the written title only after a successful logo load. A missing or failed logo must leave the written title visible; an episode retains its specific title instead of only the series logo. Verify left-aligned, full-content-width synopsis and expansion/Back, six-button Resume timestamp, directed-by/runtime/end metadata, localized text and larger fonts. Inspect bottom Media Details cards: transparent unselected surface, readable text and visible focused outline. Check repeated details updates/version changes do not steal current action focus or restore a removed button, then the existing U03/P03a controls. Compare a default theme. Source guards and a passing build do not establish these native checks.

## A02 — TV theme music in series and seasons

Dev12 candidate procedure, NOT RUN physically: enable TV theme music in Settings > Customization (default on as requested). Open a series with a server theme, listen at quiet volume, then open its season and verify no overlap. Leave to Home, background the app, start an episode, start a trailer and trigger another audio source: theme must stop. Return to series/season and verify a clean restart. A theme plays once per visit without changing the audio queue or emitting watched/progress events. Next Up episode-details pages must not autoplay theme music. Disable setting and verify silence. Start normal music first, then browse series and verify the existing music remains uninterrupted. Test absent themes, unsupported file, server/network failure and leaving before a delayed response/prepared callback. Ensure no surprise later start or crash. Missing-theme behavior is silent; file existence alone does not prove server indexing or audible playback.

Read-only source media observation 12 September: The Goldbergs and The IT Crowd have root theme.mp3 plus clearlogo.png; The Shards has clearlogo.png, no root theme file or theme-music directory found in the bounded check. No media/server changes. Retest P03a and existing audio/remote/lifecycle cases against the installed candidate; build/unit success does not establish audio acceptance.


## User-confirmed feature acceptance — 12 September 2026

Gary explicitly confirmed “Yes they work” after clarification naming Playlists artwork, TV logos and theme music including stopping on exit. These requested feature checks are USER-CONFIRMED PASS on installed dev12. Specific shows/steps were not enumerated; no independent new device capture. This supersedes earlier NOT RUN for those focused checks only. Broader U02/U03/A02 matrix and dev12 P03a playback, missing-theme/network/default-theme behavior remain unqualified. No build, installation, playback or server action in this documentation pass.

## U04 — Modern Movies/TV library browsers (dev13)

NOT RUN physically. With ElegantFin, open both Movies and TV libraries in their configured landing-page and full-grid modes. Verify the 184dp selected-item backdrop/logo/title, metadata and two-line synopsis; absent/failed logo retains title, empty library displays category artwork. Preview must not gain focus or autoplay a theme/trailer. Inspect both horizontal and vertical grids at each poster size and poster/thumb/banner mode: spacing, violet card outline, legible selected title and no focus clipping at edges. Existing options remain available.

Focus each grid toolbar button: caption must identify Sort, Unwatched, Favorites, letter jump or Settings; check white focused icon button, readable dark icon, persistent active-filter tint, status text and counter without overlap. Sorting/filtering/letter changes restart at the beginning of the new result set. Exercise no-result combinations then clear filters. Verify paging beyond the first 100 items does not unexpectedly steal toolbar focus.

Scroll several pages into a grid, note a poster and its screen position, open details and Back: same loaded item/index and scroll position must return, including repeated visits. Compare return after watched/favorite changes and library refresh where the original item may disappear. Check settings changes rebuild the correct grid, rapid Back/view recreation has no stale callback/crash, and a default theme, Music, Collections and season browser retain previous behavior. Dev12 logos/theme lifecycle and P03a remain separate regression checks. Source retention/restoration logic and build tests do not establish physical Back/focus acceptance.


### Dev14 collection preview extension

NOT RUN physically. Under ElegantFin, open an individual collection (BOX_SET), highlight different movies and series and verify each available logo replaces the preview title before entering details. Check fallback text for missing/failed logos, collection heading, long synopsis, mixed-media rows, rapid focus changes and details/Back. Preview must remain non-focusable, without autoplay. Recheck Movies/TV library previews, season logos/themes and a default theme. Poster-card text remains separate from the selected-item preview.

Dev13 user feedback, 12 September 2026: Gary said "Yeah works good" after installation and the Movies/TV/Back check invitation. General browser acceptance USER-CONFIRMED; exact U04 matrix/steps were not enumerated. Gary also reported collection movie previews retained written titles until opening details; dev14 addresses that separate browser omission.

### Dev15 logo loading presentation

Gary's dev14 Harry Potter collection check confirms logos appear inside the collection, but the written title briefly appears first. Source confirms all non-success painter states previously showed text. Dev15 reserves the fixed logo space during Empty/Loading for a known logo URL, renders logo on Success, and renders title immediately for no URL or on Error. NOT RUN physically: cold and cached Harry Potter selection, rapid D-pad changes, missing/failed logo and leaving during loading; no previous-item logo or text-to-logo flash, stable metadata position and usable text fallback. Check shared Movies/TV previews too. No broad dev14 regression acceptance inferred.

## U05 — Modern Search (dev16)

NOT RUN physically. ElegantFin: enter Search with remote, keyboard and EXTRA_QUERY route. Verify rounded high-contrast field, keyboard placeholder, voice input availability/permission behavior, and submit dismisses keyboard into results. Search Harry Potter/Aliens; movies/series/episodes/music/people remain grouped, existing click/play routes preserved. D-pad selection updates display-only backdrop/logo/title/metadata/synopsis preview; no text flash during known-logo loading. Missing/failed logo retains fallback, rapid focus does not show previous result, and preview cannot gain focus or autoplay.

Verify typing debounce, explicit immediate submission during debounce, clearing query, no-result search, server failure then same-query retry, partial-category failure message, and result count reports displayed items rather than server total (25-per-group cap unchanged). Old rows/preview must clear on query change. Some groups overlap, so count is displayed entries, not unique media. Test first-row Up to input, lower-row Up stays within results, input Left to voice, keyboard submit, details/Back, rapid new queries and leaving while requests load. No-result state must remain escapable with remote/Back.

Inspect 1080p/4K layout: 184dp preview leaves usable result rows, focus and long status/title/synopsis do not clip. Default theme should keep its prior layout. Recheck Movies/TV/collection shared preview after extraction and dev15 logo transition; playback/theme regressions remain separate. New status labels currently English with explicit untranslated fallback, not a completed localization pass.

Dev15 user acceptance: Gary answered "yes" after the Harry Potter logo-flash retest invitation. Focused logo transition USER-CONFIRMED PASS; full cold/failure/default-theme matrix not enumerated.

### Dev17 Search poster clearance

Dev16 screenshot poster-check.png under backups/20260912-180426-dev16-install confirms clipped poster tops/focus outline and crowded bottom labels for query "harry". Dev17 Search preview 128dp (was 184dp), 44dp logo, no redundant Search heading and one-line synopsis. Other preview callers retain defaults. RowsSupportFragment.setAlignment uses 36dp explicit top offset, converted to pixels; retained Leanback 1.2.0 bytecode confirms it configures item/window alignment and survives view creation. NOT RUN physically: repeat same query and selected poster; verify full top corners/violet outline, category header and bottom title/rating. Move through movie/series/other rows, verify alignment after scroll and Back, first-row Up and keyboard exit; long labels and 1080p/4K/default theme. Do not infer clipping fixed from layout constants or build alone.

Dev18 NOT RUN physically: type harry, submit and focus a result. Input/microphone/status should hide only with real results focused; preview expands from 128dp to 184dp, query becomes heading, two-line synopsis returns. Posters keep dev17 36dp alignment and gain net vertical room from removed controls. Up within lower result rows stays in results; Up from first row reaches toolbar Search; selecting Search restores input/query/keyboard after a composition frame. Repeated edit/submit cycles must preserve query and not duplicate/reset result adapter or lose focus. Moving to other toolbar buttons must not start editing automatically. No-results and initial states retain controls. Check voice recognition disposal/blur, new query/clear/failure/retry, details/Back, initial-query route, rapid focus and default-theme behavior. Confirm input does not collapse while typing merely because results arrive, and no blank focus trap or invalid FocusRequester after view recreation. No autoplay or progress edits.

## P06 — Playback HUD Stats and Remote Back Dismissal (Dev20 / Dev23)

Evidence: `PlaybackInfoOverlay.kt`, `VideoPlayerOverlay.kt`, `VideoPlayerControls.kt`.

1. During active playback on Shield TV, toggle the Info button on the playback controls bar to display the playback stats HUD.
2. Verify the 540dp landscape card presentation: deep frosted navy surface (`Color(0xEE070A12)`), 14dp rounded corners, and glowing 1.5dp violet border (`Color(0x99775BF4)`).
3. Verify the header: uppercase title `"SHIELD PLAYBACK HUD"` and play method pill badge (emerald `DIRECT PLAY`, cyan `DIRECT STREAM`, amber `TRANSCODING`).
4. Inspect the structured 2-column metrics:
   - Video column: Codec, 4K UHD / 1080p / 720p resolution badge, exact dimensions, Dolby Vision / HDR10+ / HDR10 / SDR color badge, video bitrate, frame rate, and bit depth.
   - Audio & Container column: Container format, audio codec, Dolby Atmos badge, channel layout (`7.1 Surround`, `5.1 Surround`, `Stereo`), audio bitrate, sample rate.
   - Transcode reason warning box if transcoding is active.
5. Press the Back button on the Shield remote: verify `BackHandler` cleanly dismisses the HUD card without interrupting video playback or returning to the browse screen.

Expected: Clean, non-intrusive stats display matching the ElegantFin theme, accurate live playback metrics, and immediate dismissal via remote Back.

## U06 — Library Browsing Navigation Buttons & Backdrop Retention (Dev22)

Evidence: `GridButtonPresenter.kt`, `BackgroundService.kt`, `EnhancedBrowseFragment.java`.

1. Open Movies, TV Shows, and Music library folders on Shield TV under the ElegantFin theme.
2. Navigate down to the bottom Views row (*Suggested*, *All items*, *By letter*, *Genres*, *Albums*, *Artists*, *Random*, etc.).
3. Verify button styling: Jetpack Compose `RoundedCornerShape(12.dp)`, semi-translucent dark surface (70% idle, 35% violet accent on focus), glowing 2dp violet border on focus with subtle 1dp glass border idle, standardized 44dp height and 136dp pill width, and centered 13sp semi-bold white typography.
4. Rapidly navigate left/right across Views buttons and up to media items: verify library backdrop remains stable without flashing, reloading, or dropping to black.

Expected: Modernized navigation buttons matching theme styling; smooth, flicker-free background retention.

## P07 — Audio Commentary and Subtitle Selection Popover (Dev24)

Evidence: `VideoPlayerControls.kt` (`TrackSelectionPopover`).

1. Play a title with detailed commentary tracks or long subtitle descriptions (e.g. *Alien* with multi-person commentary).
2. Open the audio track or subtitle selection popover from the playback controls overlay.
3. Verify popover layout: expanded 520dp width, max 380dp height, rounded 8dp corners, and dedicated 14dp checkmark column.
4. Verify track text wrapping: text wraps across up to 3 lines (`fontSize = 13.sp`, `lineHeight = 18.sp`, `TextOverflow.Ellipsis`) without horizontal truncation or text clipping.

Expected: Full legibility of complex commentary credits and track names; clear active track indication.

## U07 — Modern Home Layout Alignment and Portrait Posters (Dev24 / Dev25)

Evidence: `HomeRowsFragment.kt`, `CardPresenter.kt`, `ElegantFinHomePreview.kt`, `HomeFragment.kt`.

1. Open Home screen with ElegantFin enabled.
2. Inspect Movies and TV Shows rows in Next Up and Continue Watching when poster mode is configured.
3. Verify uniform 2:3 vertical portrait posters (150dp height × 100dp width) displaying series primary artwork for shows and movie posters for films.
4. Focus various cards: verify `WINDOW_ALIGN_LOW_EDGE` Leanback grid alignment anchors focused cards cleanly below the 236dp hero area.
5. Verify hero preview: 48dp logo, compacted vertical spacing, and `.clipToBounds()` preventing scrolled rows from bleeding into the hero preview area or clipping overview text, badges, or "Resume from..." line.

Expected: Uniform card aspect ratios across rows; zero vertical clipping or overlap between cards and hero preview.

## U09 — Episode identity, logo loading and parent navigation (Dev28 candidate)

Status: physical tests NOT RUN. Use an attended installation of the exact retained candidate.

1. In Continue Watching, focus a partly watched episode. Confirm series title above `S3:E3 - Are We Not Men?` (or that episode's actual numbers/name); progress must not replace episode identity. Check specials (season zero), missing numbering and long titles. Verify movie remaining-time display is preserved.
2. Scroll recently added TV selections with both cold and cached logos, including rapid direction changes. The reserved logo area should remain blank while a known logo loads, then show the correct logo; plain text should appear for a missing/failed logo. Check no stale previous logo, focus jump or changed header height.
3. Open an episode from recently added TV. Reach Go to series directly using the D-pad with both resumable and untouched episodes, with/without a previous episode and with delete permission. Open the series and another season/episode; Back should return through the previous pages. Check the previous-episode overflow action when controls are crowded and delayed/failed previous-episode lookup.
4. Open a recently added album. Reach Open artist before mix/favorite, enter the artist page and another album, then Back. Check album-artist precedence, regular-artist fallback when album-artist IDs are missing, and no invented destination when both metadata lists are absent. Confirm play, queue, shuffle and track focus remain usable.
5. Check all seven selected cinematic category banners on-TV for crop, contrast, labels and focus readability. The app uses generated originals; approval of the choice board is not physical crop/brightness acceptance.

## U08 — Episode Scene Thumbnails, Web App Parity, and Music Row Clearance (Dev26 / Dev27)

Evidence: `CardPresenter.kt`, `ElegantFinHomePreview.kt`, `HomeFragment.kt`.

1. Open Home screen on Shield TV with Dev27 installed.
2. Inspect Next Up and Continue Watching rows:
   - Verify TV show episodes display individual 16:9 scene screenshots (`ep.itemImages[PRIMARY]`), not parent series backdrops (1:1 parity with the Jellyfin web app).
   - Verify episode card labels: Line 1 displays series name (e.g. *The Goldbergs*, *The IT Crowd*), and Line 2 displays episode indexing and title (`S{season}:E{episode} - {name}`).
3. Scroll down to the Music row ("Recently added in Music"):
   - Verify row header is fully visible.
   - Verify music album covers render as 130dp square cards.
   - Focus an album card: verify glowing violet focus border, Title (Artist - Album), and Subtitle (track count) are 100% visible with generous clearance above the bottom of the TV display.
4. Verify item preview title and subtitle centering: `fillMaxWidth()` applied; smooth marquee scrolling on long titles when focused.

Expected: 100% visual parity with Jellyfin web app episode presentation; complete vertical clearance on music cards with zero cutoff.
