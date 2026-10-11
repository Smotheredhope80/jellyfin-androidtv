## GARRO-PC shared build/repackage tool configuration — 11 October 2026

P10 configured Gary's shared Android tools on GARRO-PC: existing E:/build-toolchain JDK21.0.12.1+1, Android SDK and Gradle cache; Apktool3.0.3 and JADX1.5.6 installed versioned under E:/Shared/android-apk with release asset SHA256 verification. Persistent user JAVA_HOME, ANDROID_HOME/ANDROID_SDK_ROOT, GRADLE_USER_HOME and tool PATH are configured. Ignored source/local.properties now selects E:/build-toolchain/android-sdk; original saved host-locally, signing keys untouched.

P01 Gradle9.5.1 help/configuration PASS after refreshing dependency metadata and downloading declared build plugins; initial offline/ordinary online missing-plugin failures are retained. Shared synthetic Java/DEX/resource APK build, Apktool decode/rebuild, zipalign, apksigner signature verification and JADX marker readback PASS. No P01 APK assembly, release/install/version change or physical Shield check. Existing app/build/source generations remain preserved. [Shared tool route](../../../../Knowledge/ANDROID-APK-SHARED-TOOLS.md) and [P10 results/recovery](../../../10_Platform-Reliability/04_Evidence/20261011-apk-shared-toolchain/README.md). Next: owning project chooses its bounded build task and preserves existing signing/artifact identity; restart old terminals/apps to load user environment. BEDROOM setup is separate.

---
## H: Project 01 source/worktree activation - 8 October 2026, 11:12 BST

Gary designated each PC's local `H:\Garro Homelab project\Projects\01_Jellyfin-APK` as the new Project 01 documentation and explicit agent-work home. The H `source` and `publication-worktree-20260927` are proper linked worktrees in the H `.git-store`, reconstructed by Git without replacing the J source. Both PCs have the same 164 refs, 10,714 reachable commits, branch heads and local H instruction commit `4f97ee08d2d5f07b8feaeadf3541187628e6b48a`. Post-commit `git fsck --full --no-reflogs` returned 0 on both. No APK build, device test or runtime deployment was performed. The BEDROOM P01 Antigravity project has its H folder registered; GARRO Antigravity project identity/authentication remains an agent-runtime hold. The J/E originals remain intact; the J-root documentation Scheduled Task was disabled pending an H input/output and NAS-boundary review. See `H:\Garro Homelab project\00_Project\H-ACTIVATION-20261008.md`.

Git workspace observation time: 2026-10-08 11:12 BST (both hosts; point-in-time).
Repository/worktree: each host's `H:\Garro Homelab project\Projects\01_Jellyfin-APK\source`, linked to sibling `.git-store`; publication worktree retained.
Branch or detached state and HEAD: `experiment/mobile-inspired-tv-v12` at `4f97ee08d2d5f07b8feaeadf3541187628e6b48a`; publication branch `release/shield-enhanced-v9.0-public` at `7ad5526981afc025935c25a3d77fab3a455227f6`.
Upstream and ahead/behind: no upstream configured for the source branch; ahead/behind not applicable. Existing origin/fork remotes retained; no push destination invented.
Tracked/untracked/relevant ignored state: clean tracked and untracked source worktree at observation; one relevant ignored `local.properties` retained, SHA256 `C1BB3973C7FE7B14E93682BCD0E67280770DD39FFD8C7CEDAD0B9A44604EEFF3`, matching the J source. The handoff edit is the next task-scoped Git checkpoint.
Local commits, unpublished work, or attached review: local instruction commit `4f97ee08d`, unpublished; no attached review for this H checkout was established.
Tests and qualification limits: exact ref/commit counts, worktree pointers, source/file SHA, post-commit `fsck` and Antigravity H folder registration on BEDROOM. No build/device test or live H-file agent read.
Deployment/runtime/physical acceptance state: not run; existing E/J consumers remain separately owned and preserved.
Disposition: active H source/worktree, not free for archive or retirement.
Next owner and action: use the H checkout for new P01 source work; coordinate future branch writes across PCs, qualify an upstream/transfer route and separately test any runtime or scheduled consumer before switching it.

---

## Project 01 documentation checkpoint cutover — 28 September 2026, 17:21 BST

Project 01's active operational configuration is now `J:\My Drive\Jellyfin apk\00_Project\Operational-Config\jellyfin-documents-pilot.json`, SHA256 `9AA6FA6A088B3B6A690B6C8A8559E60F96F734960B30A28EE31A678EBD72A975`. It retains the prior settings and the 17 prior document names plus Project 01's five approved additions (22 names total). This is a credential-free, machine-specific config in the project workspace, outside public Git. The original `E:\Git\garro-homelab\config-templates\backup\jellyfin-documents-pilot.json` remains intact at SHA256 `757D0F487CB606AA2A60D85D1A3B3FC16174A90EF89B891B905205A2B2F896C9` as rollback/reference; **not safe to remove**. The earlier decision section below is dated pre-cutover history and is superseded on authority and installed task path by this section.

Project 10 switched only the `GARRO Jellyfin Documentation Checkpoint Pilot` Scheduled Task's JSON argument to J:. Live readback at 17:22 BST shows the same E: wait wrapper and runner, `C:\Python313\python.exe -B`, J: JSON argument, last run 17:21 result 0 and next trigger 29 September 02:30 BST. Project 10's saved final receipt records that normalized before/after task XML differed only by that argument. Canonical local `outcome.json` readback reports `Status=verified`, `FileCount=22`, checkpoint `\\192.168.68.73\Backups\Homelab-Projects\01_Jellyfin-APK\checkpoint-20260928-162106-operator-5aff7fef`. A separate read-only invocation of `checkpoint_documents.py verify` returned 0 and `22/22`; the checkpoint `MANIFEST.json` SHA256 is `ABD5E1C2ADB3FC940B4B80C5AF22E2C1FF0FD15EE12C41DBFCBBD2165BCE39A3`. Explicit Backup Freshness report `E:\Git\garro-homelab\reports\backup\20260928-172157-backup-freshness.json`, SHA256 `0718CBA3E3D58706FB9F455AE57BB4BB63C7CFFA2852A57B112BE8AC8931AD21`, reads the new checkpoint and marks **Jellyfin Passed**. Overall checker exit 1 came from separate portfolio coverage Failed and Kopia Warning; no remediation was part of this check. Project 10 reviewed the source report reader's static behavior. These checks establish the controlled task run, file/manifest verification and explicit health-report readback, **not** the next ordinary 02:30 run, next hourly scheduled monitoring run, installed Toolbox GUI acceptance, or full APK/source/device recovery. The E: original remains required until old-path consumers and retirement gates are separately cleared.

Configuration recovery: `\\192.168.68.73\Backups\Homelab-Projects\01_Jellyfin-APK\20260928-1715-document-checkpoint-operational-config\MANIFEST.json`, SHA256 `178B8ECCA62CC8C024A676B4E936A7F1D8D26E053FB053C3A433FDB9296B8011`, indexes both exact JSON generations. The new JSON and its NAS copy matched the hash above; an independently retrieved copy at `C:\Users\garya\AppData\Local\Temp\p01-config-restore-20260928-1715\jellyfin-documents-pilot.json` matched too. The old JSON NAS copy matched `757D0F...F96C9`. This is a separate-location exact-file restore check, not independent off-NAS protection or application recovery. The NAS manifest records its pre-switch capture context; current task authority comes from the live task readback above. [Project 10 implementation card](<../../../Homelab Reliability/04_Evidence/20260928-workspace-migration/P01-IMPLEMENTATION.md>) now records the cutover and bounded monitoring evidence. Targeted local documentation before-files and selective restore note: `../../backups/20260928-172300-p01-checkpoint-cutover-docs-before/README.md`. No Shield app, server or Home Assistant action occurred; physical tests NOT RUN. Next: Project 10 checks the next hourly scheduled monitor and installed Toolbox GUI, then verifies the ordinary 29 September 02:30 task run and resulting checkpoint/monitor status before considering any E: retirement.

## V9.0 owner follow-up on Gary's Shield — 27 September 2026

Gary confirmed his Shield was the device he checked and reported: “Everything that previously worked worked, no delete button seen, no text behind logos ect”. This is an owner-observed general regression check, absence of a visible Delete button, and absence of the transient text behind home-row logos in the paths he tried. It does not identify every path exercised or establish the full U09/U10 playback, HDR/audio, More-menu, trailer fallback, or rollback matrix. Gary plans to use V9.0 normally and report any issues. No new build, installation, or device command followed this report. [Publication task](../../00_Project/ACTIVE-TASKS/20260927-v9-publication.md); [targeted document before-files](../../backups/20260927-v9-owner-followup/README.md).

## Cindy Shield V9.0 installed — 27 September 2026

Cindy's Shield at 192.168.68.85:5555 upgraded in place from dev27 to V9.0/dev30; installed version and SHA256 BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C verified. Launch succeeds but displays server setup; sign-in and general use confirmed by Gary; specific physical playback cases NOT RUN. No uninstall/data clear. Prior dev27 retained and hash-matched; downgrade untested. [Installation evidence](../../apk-inspection/20260927-cindy-v9-install/README.md), [task record](../../00_Project/ACTIVE-TASKS/20260927-cindy-v9-install.md), [pending overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-cindy-v9-installed.md). Earlier connection-blocked status is superseded. Next: record individual playback and remote-control cases if further qualification is needed.

Owner confirmation (2026-09-27 12:48 +01:00): Gary reports he signed in on Cindy's Shield and that it works like the other Shield. This confirms sign-in and general use by owner report; specific playback, trailer, remote-control and HDR/audio cases were not individually recorded. No new device command or APK change followed.

## Cindy Shield V9.0 install requested — connection blocked, 27 September 2026

Latest retained V9.0/dev30 APK hash and signature verified. Both recorded Cindy endpoints refused ADB; no install occurred. Awaiting current IP/network debugging, then identity/playback/recovery preflight and in-place update. [Resumable checkpoint](../../00_Project/ACTIVE-TASKS/20260927-cindy-v9-install.md). Physical checks NOT RUN; Gary's Shield unchanged by this task.

## Shield Enhanced V9.0 published prerelease — 27 September 2026

Gary reviewed installed dev30 and reported **“Its perfect. good enough to publish.”** This accepts the requested Delete removal and trailer remote-control experience; it does not constitute a full U10 matrix or HDR/audio playback acceptance. The same dev30 APK bytes were retained as [Shield Enhanced V9.0](../../release/shield-enhanced-v9.0/README.md) and published as an [unofficial GitHub prerelease](https://github.com/Smotheredhope80/jellyfin-androidtv/releases/tag/shield-enhanced-v9.0). Package `org.jellyfin.androidtv.debug`, version `0.0.0-dev.30`/code30, SHA256 `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C`. Public anonymous APK download returned HTTP 200 and the same 64,276,430-byte hash; release API confirmed draft=false, prerelease=true, two assets.

Public source tag `shield-enhanced-v9.0` points to sanitized commit `7ad5526981afc025935c25a3d77fab3a455227f6` on `release/shield-enhanced-v9.0-public`. Its `app`, `playback`, `preference`, `design`, `buildSrc`, `gradle` and root build-config trees match the tested dev30 source exactly; it branches from the already public V8.2 commit. Private handoff/history and the unrelated dirty `AGENTS.md` were excluded. The development branch was not pushed. V9.0 is now the latest publicly retained prerelease; V8.2 remains available as historical release, and dev29/dev28 APKs remain local recovery candidates. Cindy's Shield was untouched. Full device matrix, fallback trailers, Live TV cancellation and downgrade/restore checks remain unrecorded. [Pending shared overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-v9-published.md).

## Dev30 trailer remote shortcuts — installed on Gary's Shield, 27 September 2026

After Gary reported dev29's YouTube navigation still moved like Tab, he selected direct trailer controls: D-pad Left/Right seek ten seconds and Select toggles play/pause. Up/Down and Back still pass through to YouTube/the activity, as do dedicated media keys. The IFrame gets a focus-outline CSS rule; its standard controls remain present. [Dev30 evidence](../../apk-inspection/20260927-dev30-trailer-shortcuts/README.md) includes the exact source checkpoint, build and installed APK hashes, signature, lint and test results. Offline unit/lint/assembly passed; 46 app tests passed and the lint finding set is unchanged from dev29 (81 errors, 326 warnings, 423 hints). The four reviewed app/test files were committed as `302cd8f67` after source-manifest hash readback; unrelated `AGENTS.md` excluded. Dev30 `0.0.0-dev.30` / code30 installed at 10:53 BST on Gary's idle Shield; installed APK SHA256 `BE21C971886F73A75FD12B5C9C4D5FAE430DEABFE3F3AA7B4655DEBB2577EE5C` matches the retained candidate. App startup and populated home were observed. Gary's physical trailer shortcut/Back result is pending; U10 is not fully accepted. Cindy's Shield untouched; V8.2 remains retained release. Dev29 and dev28 APKs remain rollback resources; no downgrade performed. [Pending shared overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-dev30-delete-youtube.md). Next: record Gary's trailer result and inspect any limitation of reaching YouTube controls with Up/Down.

## Dev29 delete safety and YouTube focus — installed on Gary's Shield, 27 September 2026

Gary requested removal of the TV app's library Delete action and reported that the in-app YouTube trailer sometimes frames the whole window with a selection border. Source now removes the media Delete button, More entry, confirmation and library deletion call. The separate Live TV **Cancel series** scheduling action remains, with its dialog named accordingly. The WebView keeps D-pad focus but disables Android's default whole-view focus highlight on API 26+; YouTube's native controls and transport keys remain available. [Task checkpoint](../../00_Project/ACTIVE-TASKS/20260927-no-delete-youtube.md) and [U10 procedure](SHIELD-TEST-CATALOG.md) hold the test scope.

Dev29 `0.0.0-dev.29` / code 29 was built and installed in place on Gary's Shield `192.168.68.69:5555` at 10:00 BST. Retained and device APK hashes match: `3DF3BDC706A6819DB942DC6F1F9A282FD39E803149348B0DC3832D24076D5A2F`. Established debug certificate and package identity verified. Offline app tests, lint and assembly passed; 45 tests passed. Lint has the prior 81 errors and 423 hints, plus five new UnusedResources warnings for obsolete deletion strings (326 warnings total); no new code finding. Full results, exact source checkpoint and install evidence: [dev29 record](../../apk-inspection/20260927-dev29-safe-details-youtube-focus/README.md).

After installation, startup and populated home were observed. A movie detail screen showed Play, Play trailer(s), Watched and Favorite with no Delete control. Physical review of the More action remains NOT RUN. Gary reports the dev29 trailer remote still behaves much the same: after focus locks to the player, movement cycles through objects like Tab in Windows. Thus the focus change did not resolve the reported control experience. A brief navigation attempt accidentally entered a movie playback screen and Back was sent; no sustained playback or playback-quality verdict was recorded. Gary confirmed the existing trailer Back behavior returns to Jellyfin. Cindy's Shield was untouched. V8.2 remains the retained release; dev28 is the verified rollback APK, with no downgrade performed. Existing unrelated `AGENTS.md` edit remains separate. This dev29 finding led to the dev30 shortcut candidate above.

## Git preservation of accepted dev28 source — 27 September 2026

The 16 reviewed app files (code, tests and seven category banners) were committed as `1978b0f5103fc045f1ef16bf53d5dc07d34a2d78` on `experiment/mobile-inspired-tv-v12` after their working bytes matched the dev28 source manifest. The installed APK remains the separately verified 0.0.0-dev.28 artifact (SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E`); no rebuild or device action followed the commit. Source verification remains 45 passing app tests and the previously reviewed lint report. The independent `AGENTS.md` policy edit remains outside these commits. No upstream is configured for this branch and no push/integration has occurred. Earlier entries describing uncommitted source are dated pre-commit history.

## Dev28 owner acceptance — 27 September 2026

Gary reviewed the installed dev28 on his Shield and reported: **“Looks good. happy with this”**. Record this as owner acceptance of the delivered category artwork and UI changes. Installed artifact remains `0.0.0-dev.28` / code 28, SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E`. No additional device changes were made for this acceptance update.

The requested UI work and owner review are complete. This general acceptance does not supply individual U09 edge-case or playback regression results; those remain unrecorded. Cindy's Shield remains unchanged. Git changes remain checkpointed and uncommitted as previously documented; no release promotion or publication. Earlier next-action requests for Gary's initial review are superseded. Evidence: [acceptance record](../../apk-inspection/20260927-dev28-navigation/install-gary/acceptance/README.md).

## Dev28 installed on Gary's Shield — 27 September 2026

Authorized in chat and installed in place at 09:18 BST on Gary's Shield `192.168.68.69:5555` (SHIELD Android TV, Android 11). Preflight: launcher foreground, zero media sessions; installed dev27 hash matched the retained recovery APK. `adb install -r` succeeded. Device readback: `org.jellyfin.androidtv.debug`, `0.0.0-dev.28`, code 28; SHA256 `6E14A6165F0D5D4D6881B7BD95AFB73A54BDD5A4DF0AC39F3FCBA394AD44DA8E` exactly matches the retained candidate below. Existing signing identity retained. Official package also present before upgrade; no uninstall or data clear.

Startup through exported StartupActivity succeeded; MainActivity foreground and screenshot confirmed populated home/library and new Collections banner. Initial direct MainActivity launch was rejected as non-exported; using the normal exported entry point resolved it. No playback started. Full U09 D-pad/Back, logo-transition, parent-navigation and playback acceptance NOT RUN; screenshot/startup are not physical acceptance. Cindy's Shield was not changed. V8.2 remains retained release; dev28 is installed experimental on Gary's Shield.

Evidence: [installation record](../../apk-inspection/20260927-dev28-navigation/install-gary/README.md), version/hash/install/startup logs and home.png. Prior dev27 retained as documented below; no downgrade/restore exercised. Next: Gary checks episode labels, recent TV series navigation, recent music artist navigation and artwork on TV. [Pending installation overview](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-dev28-installed-gary.md).

## Dev28 category artwork and navigation candidate — 27 September 2026

Gary chose **A — Cinematic objects**. Seven generated originals now supply the category banners. Continue Watching episode cards retain series title plus season/episode and episode name; home preview title fallback waits until logo absence/failure; episode details keep **Go to series** visible; album details expose **Open artist** before secondary controls with valid artist-ID fallback. See [artwork/prompts](../../artwork/20260927-cinematic-categories/README.md).

**Source/artifact verification:** offline dev28 app tests, lint and assembly passed; 45 tests, zero failures/errors/skips, including six new tests. Lint remains 81 errors, 321 warnings, 423 hints. Normalized baseline comparison found only an existing WebP recommendation pointing at a different banner; no new code findings. Signature verification passed with the established debug certificate. [Candidate evidence and recovery](../../apk-inspection/20260927-dev28-navigation/README.md) includes exact APK identity, logs, test XML, lint comparison and 16 hash-verified source files. Initial build/logging and test-fixture failures are recorded there.

**Limits:** candidate NOT INSTALLED; physical D-pad/Back/lifecycle/playback, image crop and U09 checks NOT RUN. Dev27 is the last documented installed candidate (not queried today); V8.2 stays the retained release. Next: Gary selects the target for an attended dev28 install and U09 check. No server changes or publication.

**Git disposition:** observed 2026-09-27 Europe/London, active checkout `J:/My Drive/Jellyfin apk/source`, branch `experiment/mobile-inspired-tv-v12`, base HEAD `9997386bf33a7225911f3d5459e38bab0c7d1385`; no upstream/ahead-behind available. Changes remain uncommitted with verified source checkpoint; Git disposition pending. Existing `AGENTS.md` policy edit is preserved separately; no refs, toolchains, signing files or ignored runtime material changed. Targeted documentation recovery: [before-files](../../backups/20260927-category-navigation/README.md). No NAS copy/restore acceptance claimed.

[Task checkpoint](../../00_Project/ACTIVE-TASKS/20260927-category-navigation.md). [Pending overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260927-jellyfin-dev28-category-navigation.md); submission is not application.

## Git cleanup completed — 23 September 2026

The previously dirty `experiment/mobile-inspired-tv-v12` checkout is clean after two reviewed commits: source/tests/assets `30326ff9dc60ea9fbdc5170161a210a6bb035cd2`, followed by maintained guidance and evidence documents `f81a57ff1c91bdd9ddd8caa9c7beb18b2b482af0`. Git history verified with `git fsck --full --no-reflogs` exit 0 (unreachable dangling objects reported, no integrity failure). A post-commit all-ref bundle was verified and hash-manifested at `S:\Homelab-Projects\01_Jellyfin-APK\20260923-235500-git-cleanup-committed`; no restore or independent off-NAS copy was tested. This documentation-only status entry follows the build recorded below. No push or upstream integration occurred.

Next: refresh upstream refs and plan conflict resolution in an isolated checkout, then run source checks against the integrated tree. Keep the installed dev27 and V8.2 release records separate from Git cleanup; physical Shield acceptance remains outstanding where noted below.

## Git cleanup source checkpoint — 23 September 2026

Committed the accumulated native Shield implementation, tests and artwork as `30326ff9dc60ea9fbdc5170161a210a6bb035cd2` (`Preserve Shield dev27 experimental source baseline`). The commit covers 64 app/playback paths. Prior to commit, the exact working files were retained in the [NAS recovery checkpoint](../../00_Project/GIT-CLEANUP-PLAN-20260923.md). A changed-text scan found no obvious embedded key/password assignment; the seven library banners have recorded original generation prompts and resource hashes in the dev11/dev12 candidate evidence.

Verification from this checkout before the commit: `:app:testDebugUnitTest :playback:media3:exoplayer:testDebugUnitTest :app:lintDebug :app:assembleDebug -Pjellyfin.version=0.0.0-dev.27 --offline --no-daemon --no-configuration-cache` succeeded (7m 21s; 30 executed, 267 up to date). App XML: 39 tests, zero failures/errors/skips; backend XML: five passing tests, task up to date. Fresh lint report: 81 errors, 321 warnings, 423 hints; these remain open and have not been attributed to individual changes against a verified dev27 lint baseline. Mutable output APK SHA-256 `CB87E715990C075A7F9953F7E0973ADBE1F2708B7F0805CDA61388C244D7C160` matched the retained dev27 APK. `git diff --cached --check` passed. No new candidate or device acceptance is claimed; Gary's and Cindy's installed dev27 state was not rechecked. Build log and documentation before-files: [qualification checkpoint](../../backups/20260923-233000-git-qualification/README.md).

Next: commit the maintained source documents separately, then review upstream integration in an isolated checkout against refreshed refs. Physical Shield playback, navigation and audio acceptance remains separate from source qualification. Shared chronology: [pending source-commit proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260923-jellyfin-dev27-source-git-checkpoint.md); submission does not imply application.

## Dev27 installed on Gary's Shield — 15 September 2026

Target: Gary's Shield at `192.168.68.69:5555` (NVIDIA SHIELD Android TV / Android 11). Media playback was confirmed idle (`state=0`) prior to install.
`adb install -r` succeeded (Streamed Install). Installed `org.jellyfin.androidtv.debug` / `0.0.0-dev.27` / code 27. Device APK SHA256 `cb87e715990c075a7f9953f7e0973adbe1f2708b7f0805cda61388c244d7c160` matches candidate APK `apk-inspection/20260915-dev27-music-text-episode-thumbs/jellyfin-androidtv-v0.0.0-dev.27-debug.apk` (67,583,603 bytes) exactly. Established debug certificate SHA256 `d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13`. Warm startup launched successfully; foreground confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Screenshots verified across all rows.

Dev27 enhancements:
1. **Individual Episode Thumbnails (Web App Parity)**: In `CardPresenter.kt`, TV show episodes in Next Up and Continue Watching rows now prioritize the individual episode scene thumbnail (`ep.itemImages[PRIMARY]`) rather than falling back to series/parent backdrop. Screen captures on Gary's Shield confirmed 100% parity with Jellyfin web app: *The Goldbergs* (Barry and Adam on couch), *The Shards* (party scene), *The IT Crowd* (Moss under table), *Forensic Files II* (burning bed), *Sabrina* (cake scene), *How I Met Your Mother* (Lily and Robin), *True Blood* (graveyard), and *Robot Chicken* (claymation Idol scene).
2. **Web App Parity Episode Text**: Top line displays the series name (e.g. "The Goldbergs", "How I Met Your Mother"), and subtitle displays `S{season}:E{episode} - {name}` (e.g. "S1:E4 - Why're You Hitting Yourself?", "S2:E2 - The Scorpion and the Toad") or remaining playback time during active progress.
3. **Music Row Bottom Text & Vertical Space**: Adjusted the home hero preview height from 236dp to 168dp in `ElegantFinHomePreview.kt` and `HomeFragment.kt`, providing 68dp (136px) of additional vertical clearance in `HomeRowsFragment`. Standardized music album covers to 130dp square in `CardPresenter.kt`, ensuring full visibility of both Title ("Massive Attack - Heligoland") and Subtitle ("10 tracks") with generous clearance above the bottom of the TV display.
4. **Marquee & Alignment**: Added `Modifier.fillMaxWidth()` to `ItemPreview` title and subtitle texts, ensuring text is properly centered and marquees smoothly on focus.

Installed status: Dev27 (`0.0.0-dev.27`, code 27) is installed experimental on both Gary's Shield (`192.168.68.69:5555`) and Cindy's Shield (`192.168.68.68:5555`). V8.2 remains latest retained release.
- **Gary's Shield (192.168.68.69:5555)**: Installed 2026-09-15 16:32 BST. Candidate APK SHA256 `cb87e715990c075a7f9953f7e0973adbe1f2708b7f0805cda61388c244d7c160` verified on-device. All rows visually verified via live screen capture.
- **Cindy's Shield (192.168.68.68:5555)**: Installed 2026-09-15 22:15 BST. Upgraded in-place from Dev18 (`code 18`) via `adb install -r`. Package dumpsys verified `versionCode=27` and `versionName=0.0.0-dev.27`, warm launch confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`.
- **Server Baseline**: Jellyfin Server on GARRO-NAS upgraded to stable `12.1.0` on 2026-09-16 22:10 BST; client compatibility verified.
Rollback resource: Dev26 (`apk-inspection/20260915-dev26-home-headers-webapp-parity/jellyfin-androidtv-v0.0.0-dev.26-debug.apk`, SHA256 `896f72edc0cb25c99890ce35b0eaa0ad4e4972152f2774b0abd9d7b511a94dec`), Dev25, Dev24, Dev23, Dev22, Dev20, Dev19, and Dev18.
Evidence and targeted before-files: `../../backups/20260915-172500-music-text-episode-thumbs/`. Active local non-secret checkpoint; no NAS copy claim.
Overview updates:
- `Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev27-installed-gary-shield.md` (Status: applied by Homelab automatic updater, 2026-09-15T23:27:19+01:00, Master SHA256 `3A8C9539...`).
- `Knowledge/OVERVIEW-UPDATES/20260915-221500-jellyfin-dev27-installed-cindys-shield.md` (Status: applied by Hermes Agent, 2026-09-15T22:15:00+01:00).
Next: Gary and Cindy verify daily playback and home screen browsing on Dev27 against Jellyfin Server 12.1.0.

## Dev27 installed on Cindy's Shield — 15 September 2026 (22:15 BST)

User authorized updating Cindy's Shield (`192.168.68.68:5555`) with the latest custom Jellyfin APK. Media playback confirmed idle (`state=0`) prior to installation.
`adb install -r` succeeded (Streamed Install). Updated `org.jellyfin.androidtv.debug` from Dev18 (`code 18`) to Dev27 (`0.0.0-dev.27` / `code 27`). Device package dumpsys verified `versionCode=27` and `versionName=0.0.0-dev.27`. Warm app launch succeeded and foreground focus confirmed `org.jellyfin.androidtv.ui.browsing.MainActivity`. Dev27 features (individual episode scene thumbnails, web app parity episode numbering/text, music row clearance, centered item preview text) active on Cindy's Shield. Overview update applied with receipt: `Knowledge/OVERVIEW-UPDATES/20260915-221500-jellyfin-dev27-installed-cindys-shield.md`.

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
Proposed overview update: [`Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev25-installed-gary-shield.md`](file:///J:/My%20Drive/Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev25-installed-gary-shield.md) (Status: pending).
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
Proposed overview update: [`Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev24-installed-gary-shield.md`](file:///J:/My%20Drive/Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260915-jellyfin-dev24-installed-gary-shield.md) (Status: pending).
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
1. **Gary's Shield (`192.168.68.69`)**: Debug `0.0.0-dev.18` (code 18) installed in place (`adb install -r`) and physically verified. The dynamic Search input behavior passed acceptance: focusing into the search result group hides the query/input controls, expanding the preview area to 184dp with query heading and two-line synopsis; pressing Up to the persistent Search toolbar button restores the search input field and re-requests keyboard focus. Movie poster outline clearance is confirmed.
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
## Dev16 Search poster clipping observed — 12 September 2026

Gary reported slight clipping at the top of posters. Fresh screenshot of Search query "harry" shows 37 results, selected Harry Potter logo/metadata/synopsis and the results row. Top edges of posters and selected violet outline are cut off at the result viewport; first-row heading is not visible, and bottom title/rating area is crowded. This is observed UI clipping, not a proven source-art issue. Source has a 184dp preview above existing RowsSupportFragment/default alignment with only 5dp result top padding. Exact crop depth and final correction not yet measured/tested. Next correction should rebalance preview/result height and Leanback alignment/focus clearance; do not just crop/replace artwork. Evidence: ../../backups/20260912-180426-dev16-install/poster-check.png. Search results and selected preview visibly load; full U05 focus/voice/Back and failure matrix still not qualified. No code/build/device navigation action during this read-only screenshot diagnosis.
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
# Project handoff

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

## Custom library artwork proposals — 12 September 2026

Created six matching text-free 3:1 PNG banners with the built-in image generator: Movies, TV Shows, Music, Collections, Music Videos and Music–Staging. Artistic direction inferred from fresh read-only directory-name samples in Z:\Movies, Z:\TV Shows and Z:\Music: retro science fiction, gothic cinema, industrial/electronic music and nostalgic television. Presence in the library is not a ranked preference; Music Videos/Staging contents were not separately sampled.

[Artwork and provenance](../../artwork/20260912-library-categories-v1/README.md), [exact prompts](../../artwork/20260912-library-categories-v1/PROMPTS.md), [asset hashes](../../artwork/20260912-library-categories-v1/MANIFEST.csv) and garro-library-artwork-v1.zip are retained together. All six actual outputs are 2172×724, visually inspected and copied with matching SHA-256 hashes; no resizing or upscaling. No server image replacement, APK edit/build/install, playback or progress changes. Shield crop/brightness acceptance NOT RUN. Dev10 installation and its open acceptance limits above are unchanged.

Next: user artwork review, then choose APK-only or server-library application before deployment. Small local non-secret documentation before-files: ../../backups/20260912-150552-category-artwork-docs; no NAS backup or restore-test claim. [Shared overview proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-category-artwork.md), not assumed applied.

## Dev10 installed; visual acceptance incomplete — 12 September 2026

Gary explicitly authorized installation. Shield identity, stopped playback and installed dev9 hash matching rollback verified. In-place dev10 install succeeded; device code 10/hash 830051711DAC964CBEE6084191BA6B48811DB5C6CD491601CE67822FACFD7733 match candidate. COLD/Status ok launch and signed-in Home observed. [Installation evidence](../../apk-inspection/20260912-dev10-artwork-polish/installation.md).

U02/U03 PARTIAL: library hint/duplicated text removed, but first Home screenshot has a blank preview area. Subsequent navigation reached Collections/Ace Ventura rather than expected Continue watching, making the sequence inconclusive; cause not established. Final screenshot is collection browser, not FullDetails. Stopped remote key input and left user control; no agent-started playback. Movie logo/description, transparent info cards, clipping/focus and dev10 P03a remain unverified.

Next: controlled attended Home/artwork and movie-details check, investigate blank initial library preview if persistent. Dev10 installed experimental; dev9 retained rollback (downgrade untested); V8.2 latest retained release. Before-files/evidence: ../../backups/20260912-141933-dev10-install, local non-secret checkpoint, no NAS/restore claim. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev10-installed.md), not assumed applied.

## Dev10 user-feedback polish candidate ready — 12 September 2026

Implemented Gary's four refinements: full-width top-aligned Home artwork behind the toolbar; artwork-only library preview without duplicated text/hint or left fade; movie/series logo replaces written title after successful load, with fallback and full-width left-aligned synopsis; transparent ElegantFin Media Details cards with a visible focus outline. Expanded action row/label capacity and metadata slots address dev9 clipping. Details rebinds avoid detaching identical focused actions and reject stale detached focus targets; physical focus retest still required.

Dev10/code 10 candidate SHA-256 830051711DAC964CBEE6084191BA6B48811DB5C6CD491601CE67822FACFD7733. Complete identity/source/validation/rollback: [build notes](../../apk-inspection/20260912-dev10-artwork-polish/README.md). Build exit 0 in 6m 16s, 32 fresh app tests pass, five unchanged backend tests UP-TO-DATE with passing XML. Signature, diff and source snapshot checks pass. Lint 79 errors/316 warnings/422 hints: removed unused hint/MissingTranslation error; one new UseKtx style warning. Four unnecessary-safe-call compiler warnings documented. No suppressions or clean-lint claim.

NOT INSTALLED; no device/playback/progress/server operation. Dev9 remains last verified installed and retained prospective rollback (downgrade untested); V8.2 latest retained release. All dev10 physical U02/U03/P03a cases NOT RUN. Catalogue now covers top-edge artwork/crop, library transparency, logo success/failure, full-width description, six-action labels, media-card focus and refresh/default-theme regressions.

Before-files: ../../backups/20260912-140508-dev10-artwork-polish, small active local non-secret checkpoint, no NAS/restore claim. [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev10-artwork-polish.md), not assumed applied. Next: authorized attended installation after fresh target/package/playback checks, then native visual/focus qualification. Do not infer physical fixes from source checks alone.

## Dev9 installed; details acceptance partial — 12 September 2026

Gary explicitly approved installation. Reverified Shield Android TV, stopped playback, dev8/code 8 and installed hash matching retained rollback. Dev9 installed in place successfully; device version/code/hash match retained candidate 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D. Cold launch returned Status ok and signed-in Home loaded. Details/evidence: [installation record](../../apk-inspection/20260912-dev9-modern-details/installation.md).

U03 PARTIAL: modern Aliens details renders with readable focus contrast, but Resume timestamp is clipped with six visible actions; DIRECTED BY wraps beyond its metadata slot. Saved 1:02:18 remains present in hierarchy/Home. Initial Resume focus recorded; later intended action sample found focus in More like this, making controlled action restoration inconclusive. Cause not established. Back restored Aliens on Home. No playback started; dev9 P03a and broader TV/default-theme/lifecycle cases NOT RUN. Home left open.

Next: fix label/metadata clipping and investigate delayed details focus before next build, then repeat attended U03/P03a. Dev9 installed experimental; dev8 retained rollback (downgrade untested); V8.2 latest retained release. Before-files: ../../backups/20260912-134927-dev9-install (local non-secret checkpoint, no NAS/restore claim). [Shared proposal pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev9-installed.md), not assumed applied.

## Dev9 modern details candidate ready — 12 September 2026

Implemented Gary's next movie/TV details stage in native ElegantFin presentation: poster-led hero, larger two-line title, metadata/synopsis and responsive rounded action buttons with explicit focus contrast. Existing Resume/Play/Trailer handlers and focus restoration preserved; Trailer gains overflow priority. Next Up/Upcoming/Next Episode rows use landscape cards and saved remaining time. Season browser stays unchanged; default themes and other detail types retain original layouts.

Dev9/code 9 candidate built and signature-verified: SHA-256 88482C48F490CF59C9DDE17ABF923F5480155079550D6F47C2FA2EF72D1F554D. Complete identity/source checkpoint/validation/rollback: [candidate notes](../../apk-inspection/20260912-dev9-modern-details/README.md). Build exit 0 in 5m 48s; 32 fresh app tests passed, five unchanged backend tests UP-TO-DATE with passing records. Lint 80 errors/315 warnings/422 hints, no new issues versus dev8; existing library-hint translation debt remains. Diff check passed.

NOT INSTALLED; no device operation or playback/progress/server change this pass. Dev8 remains last verified installed; retained APK hash-checked before build, prospective rollback after dev9 (downgrade untested). V8.2 remains latest retained release. All dev9 physical UI/playback checks are NOT RUN. Added U03 to the catalogue for layout, long/localized labels, overflow, artwork, D-pad/Back, season/episode navigation and default-theme/lifecycle checks; P03a still required for playback.

Before-files: ../../backups/20260912-133606-dev9-modern-details (small active local non-secret checkpoint, no NAS/restore claim). Shared overview submission: [dev9 pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev9-modern-details.md), not assumed applied. Next: authorized attended installation and U03/P03a after fresh target/package/active-playback checks. Preserve the current session until that boundary.

## Dev8 installed; focused home fixes and Aliens Resume passed - 12 September 2026

Completed the next home-polish/qualification stage Gary requested. Library shortcut text is explicitly white. Library selection now has a title/artwork/hint instead of a blank preview. Selected preview state is observed in a separate composable so its updates do not recompose the embedded Leanback row container. Playback/progress implementation is unchanged.

Dev8/code 8 built, signature-checked, installed in place and device-hash verified: 7E5378AA0B34A99A9D45E9728C16FDA40636BD5EA7F0CC435DC68A8F05156F66. Retained APK, exact source checkpoint and complete evidence: ../../apk-inspection/20260912-dev8-home-polish/README.md. 32 fresh app tests passed; five unchanged backend test records UP-TO-DATE. Lint 80 errors/315 warnings/422 hints: only new finding is the new English library hint missing other translations; no suppression added. Assembly/signature/diff checks passed.

Shield screenshot verifies readable labels and Collections preview. Controlled focus checks passed: library/movie rows, left/right movie selection, first-row Up to toolbar, return Down, and details/Back restoring Aliens. Fresh Aliens Resume 1:01:15 yielded advancing PLAYING positions consistent with saved progress; Gary confirmed expected scene and normal picture/sound. Playback left running. Initial post-install capture showed launcher; subsequent waited launch succeeded, with no matching trace in the limited crash-buffer query. Broader startup/default-theme/error/rapid-repeat/performance and media matrix remain untested; do not call this complete release qualification.

Dev8 is installed experimental; dev7 r2 retained rollback (downgrade untested); V8.2 latest retained release. Before-files/device evidence: ../../backups/20260912-131525-dev8-home-polish. Small active local checkpoint, no NAS/restore claim. Shared overview submission: [dev8 pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev8-home-polish.md), not assumed applied.

Next stage: modernize movie/TV details pages while preserving native Resume/Play/trailer actions. Translation and broader home qualification remain tracked follow-ups. Preserve the successful playback session.

## Dev7 installed; native home acceptance partial - 12 September 2026

Gary authorized installation. Reverified Shield Android TV, dev6/code 6, stopped playback and matching retained dev6 APK hash. Installed final dev7 r2 in place successfully. Device version/code and SHA-256 match the retained 2ED5AC444FFB7257B1776C4F8DC2ECAEA974021B1822635F3C5298E31F0A181A APK. Signed-in Home loaded; Aliens artwork/logo, source-format badges, synopsis and Resume 1:01:15/progress/remaining time rendered on the actual Shield. Playback was not started. See ../../apk-inspection/20260912-dev7-modern-home-r2/installation.md for evidence and limits.

U02 PARTIAL: library shortcut text is too dark; initial library focus leaves the preview empty. A later Right-key hierarchy returned library focus, so controlled focus-restoration acceptance remains open. Next: fix shortcut text contrast, review preview/focus behavior, and complete attended U02/P03a. No claim of full native UI or playback acceptance. Dev7 is installed experimental; dev6 retained rollback (downgrade untested); V8.2 latest retained release.

Before-files/install evidence: ../../backups/20260912-130039-dev7-install. Shared overview: [dev7 installation pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev7-installed.md), not assumed applied.

## Dev7 Balanced native home candidate ready - 12 September 2026

Gary approved the Balanced mockup and explicitly requested implementation. Implemented the native ElegantFin home: selected-item backdrop/title logo with text fallback, metadata/format badges, synopsis and saved Resume label; 90dp landscape cards with progress/remaining time; compact library shortcuts. Configured section ordering and native toolbar/focus/launch handling remain. Default themes retain previous presentation/query behavior. See [design record](MODERN-HOME-DESIGN.md).

Final candidate: ../../apk-inspection/20260912-dev7-modern-home-r2/jellyfin-androidtv-v0.0.0-dev.7-debug.apk, package org.jellyfin.androidtv.debug, code 7, SHA-256 2ED5AC444FFB7257B1776C4F8DC2ECAEA974021B1822635F3C5298E31F0A181A. Assembly/signature passed; established certificate preserved. HEAD plus exact tracked/untracked checkpoint and complete evidence are in the candidate folder. Resume source/selection helper unchanged. First Java constructor compatibility failure and draft Compose lint error were fixed; superseded draft retained separately, not for installation.

Validation: 32 fresh app tests passed; five unchanged backend tests UP-TO-DATE with passing records. Final lint 79 errors/315 warnings/422 hints; no findings in changed home files. Only new baseline differences are three toolchain-version warnings in untouched libs.versions.toml. Git diff check passed. See candidate README for exact command, provenance and limits.

Dev6 remains the last verified installed build; no device query/control/install or server/progress mutation in this pass. Physical U02 home navigation/artwork/layout/performance/default-theme checks and dev7 P03a are NOT RUN. Dev6 retained as prospective rollback; downgrade not tested. V8.2 remains latest retained release. Next: attended install/test after target and playback boundary are confirmed. Installation is not inferred from this source/build authorization.

Before-files: ../../backups/20260912-123624-modern-home-native (small active local checkpoint, no NAS/restore claim). Shared overview submission: [dev7 candidate pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev7-modern-home-candidate.md); not assumed applied.

## Modern home mockup ready for review - 12 September 2026

Gary approved the proposed interactive TV-sized home mockup. The preview now includes selection-driven artwork and title logos, Continue watching progress cards, library shortcuts, local details and simulated playback controls. Balanced is the proposed default layout. See [MODERN-HOME-DESIGN.md](MODERN-HOME-DESIGN.md) for the design, exact preview/evidence paths, implementation mapping and limitations.

Browser interaction and layout checks passed at 320, 736 and 1024 pixel content widths, including first-row Up, lower-row Up, details Back focus restoration, search, resume versus beginning, and long-title fallback. No browser errors. Progress and format values are illustrative; Aliens 31:39 references the earlier test, not current playback. This pass created a design preview and documentation only: no production source change, APK build/install or Shield operation. Dev6 remains the last verified installed identity recorded below.

Targeted documentation before-files: ../../backups/20260912-122322-modern-home-design. Small local checkpoint; NAS protection and restore testing are not claimed. Shared overview proposal: [modern home mockup pending](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-modern-home-mockup.md). Submission does not establish application to the master.

Next: review the design, then implement the agreed native home layout as the next candidate while preserving the accepted resume fix and existing APK rollback resources. Physical Shield acceptance remains a later step.

## Dev6 installed and Aliens resume accepted - 12 September 2026

Gary explicitly approved installation after advancing Aliens to about 30 minutes. The verified Shield was playing Aliens (1986), Special Edition, 2160p/TrueHD Atmos. Agent paused and exited playback gracefully at 1,899,387ms (31:39.387), then installed dev6 with adb install -r: Success. No uninstall or data clearing. Installed package org.jellyfin.androidtv.debug, version 0.0.0-dev.6/code 6, device-side APK SHA-256 9AB569CC304B14E2D235399C45908A09234BA7A9836EF800DDC4A3424FC2DB81, matching the verified retained candidate. Certificate remains d2e58d83d659813ec0264c831535320d3f40659068017666be17d1a5077aeb13.

Reopened with the package-scoped jellyfin://launch intent; signed-in Home and Continue watching loaded without a login prompt. Preferences XML and legacy authentication XML hashes matched pre-install. authentication_store.json hash changed during the transition; its contents were not inspected or retained, so no claim of byte-identical private app data. Functional signed-in access and preferences preservation passed.

Fresh Aliens details displayed Resume from 31:39. Agent activated that focused Resume button. First MediaSession readback: PLAYING at 1,899,393ms; second readback: PLAYING at 1,917,174ms, speed 1.0, error=null. Gary confirmed: Yes, correct scene and normal picture/sound. P03a Aliens Resume case PASS for this installation/session. Playback was left running after confirmation. Pre-install details lacked a Resume button until returning through refreshed Home/details; subsequent server-backed Continue watching/details showed the captured position. No direct server progress rewrite was performed.

Dev6 is now the installed experimental build; V8.2 remains the latest retained release. Dev5 is retained as the immediately prior, verified matching-signature rollback candidate (downgrade not tested). Build qualification remains 34 tests passed, signature/assembly passed, lint 79 errors/312 warnings/422 hints with no findings in resume-change files. Other movies, cold-start/repeated-resume, Play-from-beginning, next-item and full HDR/audio/control matrix remain NOT RUN physically; one attended success does not establish complete release qualification.

Evidence: ../../apk-inspection/20260912-dev6-resume/installation.md and install-checkpoint-path.txt; the linked dated backups checkpoint contains install output, installed-version readback, private-file hashes (no secrets), Resume label and both playback position readbacks. Source/APK provenance remains in the candidate README and CURRENT-RELEASE.md. Shared overview submission: ../../../Garro Homelab project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev6-installed-resume-pass.md (pending, not assumed applied).

Next: continue normal use of dev6 and record any recurrence with title, displayed Resume timestamp and actual start position. Broader physical cases remain open; do not interrupt Gary's successful playback for additional tests without a new attended boundary.


## Earlier pre-install investigation - 12 September 2026

Gary reports Aliens and likely other movies restart from the beginning despite the Resume button displaying the expected timestamp. Debug access was initially rejected, then Gary approved the Shield prompt and read-only verification succeeded: SHIELD Android TV, Android 11, org.jellyfin.androidtv.debug 0.0.0-dev.5/code 5. The installed base.apk SHA-256 is 1349E8E0B6769881E5379F03A29EF86A278A61D736363A67D8C0C5A5535AD667, matching the retained dev5 migration APK and prior mutable build output. Selective preference inspection found playback_new=true; external_player and pref_resume_preroll were absent (source defaults false/0). Credentials were not retained.

Source diagnosis: the active rewritten player VideoPlayerFragment queued asynchronous stream loading and immediately called state.seek. ExoPlayerBackend later selected the prepared movie; the early seek could target an empty/previous playlist and be reset by movie selection. The legacy initialSeek/unseekable branch initially investigated is not the active path on this Shield. No matching recent seek log was available; physical reproduction remains pending.

Implemented candidate: attach initialPosition to the first queue entry, keep it through asynchronous resolution/preparation, and consume it at indexed media selection before play. Clamp negative preroll to zero; preserve explicit Play-from-beginning zero; later queue entries and repeated playback do not inherit the initial resume seek. Existing pre-task theme/trailer work remains intact. New ResumeQueueTests and ResumeSelectionTests cover queue retention/consumption, first-item isolation and indexed backend selection. P03a defines attended regression steps. Dev6 verification completed: 34 tests passed (29 app, 5 backend), assembly/signature and diff check passed. Lint: 79 errors/312 warnings/422 hints; the five-error increase versus the older baseline is in untouched pre-existing trailer key handling, with zero findings in resume change files. See candidate README and CURRENT-RELEASE for exact APK identity and limitations. No installation or release promotion.

Source changes and exact dirty checkpoint: ../../apk-inspection/20260912-dev6-resume (HEAD, tracked patch, untracked source). Targeted before-files and build evidence: ../../backups/20260912-111957-resume-investigation. See CURRENT-RELEASE.md for candidate status as verification completes. Rollback source files individually using the checkpoint; preserve subsequent changes. No server/progress/settings mutation was made, no live test playback was started, and physical tests remain NOT RUN.

Next: scoped installation/attended P03a approval for verified dev6. Gary has prepared Aliens at approximately four minutes for testing. In-place installation will close the app and interrupt playback; verify playback stopped first. Debugging-connection authorization does not imply installation or playback authorization.

Shared overview proposal: [pending resume investigation](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-apk-resume-investigation.md).

## Workspace relocation - 9 September 2026

Primary workspace is now `J:\My Drive\Jellyfin apk`; Git and Gradle root is its `source` directory. See `../../00_Project/migration/README.md` for verification and source-retirement status, and `../../00_Project/BUILD-ENVIRONMENT.md` for retained JDK/SDK setup. Pre-existing source changes and release records are preserved. The workspace move does not establish new APK/device acceptance or exact historical dev5 source provenance.

Historical homelab integration notes and the banner screenshot are available under `../../01_References` with a capture manifest. Read the shared Knowledge hub for current cross-project context. Ava references in historical notes describe the superseded first voice-replacement attempt; current household work uses View Assist/VACA.

Updated 2026-09-09 after a documentation-only evidence audit. No APK rebuild, installation, physical Shield test, Git commit or publication occurred.

## Historical state at 9 September audit

Latest retained release is **V8.2**; latest discovered experimental APK is **dev5/code 5** in build output. **Gary confirmed on 2026-09-09 that `0.0.0-dev.5 debug` is installed on the Shield now.** The installed hash/signature/code were not independently read from the device. `CURRENT-RELEASE.md` separates measured artifact identity, user confirmation and historical test claims.

Git branch at inspection: `experiment/mobile-inspired-tv-v12`; HEAD `5abaa15745afe5016f2ae12f88845794ffe5ff54` (2026-09-06 00:40:19 +01:00, theme cold-start fix), also the tip of `experiment/embedded-trailers-elegantfin`. The active branch name does not establish a V12 release or a server version.

These eight pre-existing source/test changes were preserved and remain uncommitted:

| Status | Path relative to source |
|---|---|
| Modified | `app/src/main/java/org/jellyfin/androidtv/ui/background/AppBackground.kt` |
| Modified | `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeFragment.kt` |
| Modified | `app/src/main/java/org/jellyfin/androidtv/ui/home/HomeRowsFragment.kt` |
| Modified | `app/src/main/java/org/jellyfin/androidtv/ui/playback/EmbeddedTrailerActivity.kt` |
| Modified | `app/src/main/java/org/jellyfin/androidtv/ui/presentation/CardPresenter.kt` |
| Untracked | `app/src/main/java/org/jellyfin/androidtv/ui/home/ElegantFinHomePreview.kt` |
| Untracked | `app/src/main/java/org/jellyfin/androidtv/ui/home/HomePreviewViewModel.kt` |
| Untracked | `app/src/test/kotlin/ui/playback/TrailerMediaKeyTests.kt` |

The five tracked changes account for 162 insertions/51 deletions at inspection. Do not stage, overwrite or discard these as part of unrelated work. The four new guidance documents are separate additions.

## Completed work supported by evidence

- Historical PR5743 base `df90e9814` introduced manual HDR capability overrides; parent BUILD-INFO and test instructions document separate Profile 8 and Profile 7 scenarios.
- `d29768c18` / `shield-enhanced-v0.1-rc1` records the Shield Enhanced feature baseline: DV7/Media3 handling, pause artwork/timing, friendly track selection, subtitle styling, playback speed and visual chapter browser.
- `e6de1bdfb` / V8 restores remote capability registration on foreground resume with bounded retries. V8 notes record successful installed upgrade, capability registration and Batman remote playback.
- `ad1a934c4` adds the stable `jellyfin://launch` link. `7c685b6e5` / V8.2 updates the full Jellyfin debug wordmark; V8.2 APK/checksum and banner screenshots are retained.
- `7094ede8a` prototypes embedded YouTube trailers and an ElegantFin-inspired native theme; `5abaa1574` fixes a theme background cold-start crash.
- Dirty source now includes the selected-item home preview/state bridge, card styling, 256px fallback background raster size, distinct trailer PLAY/PAUSE commands, bounded seek, and WebView lifecycle/dispatch changes. These are existing implementation changes, not newly authored by this documentation task.
- This task created `AGENTS.md`, `docs/CURRENT-RELEASE.md`, `docs/SHIELD-TEST-CATALOG.md` and this file from local evidence, and reviewed their claims against retained source, Git refs, APKs and reports.

## Latest validation and limits

All six inspected release/experimental APKs passed fresh v1/v2 signature verification and share one Android Debug certificate. V8/V8.2 hashes agree with retained checksum files. Dev5 metadata agrees with version code 5. Exact artifact identities are centralized in CURRENT-RELEASE.

Existing unit XML reports dated September 8 record 25 tests with zero failures/errors/skips, including three trailer-media-key tests. They were not rerun and do not establish physical focus/playback or exact dirty-source-to-APK reproducibility. Existing lint output is dated September 5 and contains 74 errors, 315 warnings and 422 hints; the Gradle configuration allows lint task completion despite errors. No fresh lint result for the current dirty work was established.

Retained `../apk-inspection/jellyfin-dev5-home.png` and `jellyfin-dev5-dpad-right.png` are limited visual evidence. The retained dev4 APK is older capture evidence; Gary confirms dev5 now installed. Device model/firmware, display/audio configuration and full dev5 device acceptance remain unverified. The shared overview records V8.2 installation, launch/remote playback and banner checks, and server 12.0.0 on September 8; these are historical records, not fresh device/server queries.

## Known issues and documentation conflicts

1. Exact dev5 build provenance and source checkpoint are missing. HEAD alone excludes eight pre-existing edits. Preserve the current source and APK before further development.
2. The committed trailer prototype toggled even dedicated PLAY/PAUSE keys; the dirty implementation corrects that and has mapping tests. Physical Shield transport keys, YouTube D-pad/Back, focus return, embed denial and lifecycle still need qualification.
3. Lint has retained findings and no current baseline comparison; do not call it clean or infer every finding is introduced by this experiment.
4. `docs/shield-enhanced.md` has an RC1 installation example and broad historical testing/privacy statements. Treat it as a feature baseline, not a current installation instruction or fresh privacy audit.
5. `BUILD-INFO.md` is August 29 history, not automatically current. Its Profile 8 objective and the separate Profile 7 instructions must not be merged into one override setting.
6. Initial experiment instructions propose dev3; dev4/dev5 now exist. Inspect version codes before choosing a new upgrade version.
7. V8.1 has no tag/adjacent notes/checksum. Its association with the launch-link commit is unverified. V8.2 has documented installation/remote-playback/banner evidence in the shared overview, but no comprehensive HDR/trailer test matrix for dev5 is established.
8. Shared `J:\My Drive\Garro Homelab project` is accessible. The previously missing `Knowledge` directory was created and populated at Gary's explicit request with README and Jellyfin Shield cross-project guidance. The chronological overview was read and preserved unchanged.
9. The RC1 publication checklist's 'nothing published' statement is superseded by the shared overview's records of RC1 and V8.2 publication/download verification. Current public availability was not queried.

## Relevant files and preserved resources

- `AGENTS.md`: durable workflow and evidence hierarchy.
- `docs/CURRENT-RELEASE.md`: release/candidate identity, dates, tests, unknowns and rollback boundaries.
- `docs/SHIELD-TEST-CATALOG.md`: build, HDR, controls, resume, trailer, focus and rollback procedures.
- `docs/shield-enhanced.md` and `EXPERIMENT-EMBEDDED-TRAILERS-ELEGANTFIN.md`: historical feature/experiment scope.
- App `ui/home`, `ui/presentation/CardPresenter.kt`, `ui/background/AppBackground.kt`, `ui/playback/EmbeddedTrailerActivity.kt`, `util/sdk/TrailerUtils.kt`: current UI/trailer work.
- App `data/eventhandling/SocketHandler.kt`, `AndroidManifest.xml`, `preference/constant/HdrFormat.kt`, playback overlay and subtitle settings: integration/HDR/control behavior.
- `app/build/test-results/testDebugUnitTest`, `app/build/reports`, `app/build/outputs/apk/debug`: retained reports and mutable dev5 build output.
- Parent `release/shield-enhanced-v0.1-rc1`, `shield-enhanced-v8`, `shield-enhanced-v8.1`, `shield-enhanced-v8.2`: retained APK history (the last three names are also under `release`).
- Parent `output/jellyfin-androidtv-pr5743-hdr-override-debug-df90e98.apk`, root V2–V7.6.3 APKs, `BUILD-INFO.md`, `SHIELD-TEST-INSTRUCTIONS.txt`: older evidence, preserved.
- Parent `backups/V7.6.3-source-checkpoint-2026-08-31`: source checkpoint, not an app-data backup. Parent `toolchain` and `toolchains`: retained SDK/JDK/Gradle resources; do not clean them during unrelated work.

## Rollback and recommended next task

Preserve V8.2 as the pre-experiment rollback baseline. Its APK is `../release/shield-enhanced-v8.2/Jellyfin-AndroidTV-Shield-Enhanced-V8.2-debug.apk`, code 2, hash `AADAAF46599D5A8F502540BA7646D10F4CD87A77BD1398CB0EF1F90697F900C2`. Dev4 is additionally retained in apk-inspection; its usability is unqualified. Older V8/V7.6.3 resources remain available.

Returning from code 4/5 to code 2 is a downgrade. Use catalog X01 during authorized rollback; shared signing does not guarantee plain `install -r` or app-data compatibility. Do not uninstall/clear data automatically on failure. The separate official app is an alternative, and the source checkpoint does not restore device settings.

**Recommended next task: qualify and checkpoint the existing mobile-inspired experiment before further feature work.** Preserve dev5 and the dirty source under unique archival names, review the eight changes, run current build/unit/lint verification with explicit version selection and baseline comparison, then perform an attended Shield acceptance session using U01, T01–T03, P01/P03–P05 and R01. Identify the installed build and confirm rollback before installation. Record actual results and only then decide whether to promote a new retained release. Keep server or Home Assistant configuration changes outside this task unless separately requested.










Dev13 installation overview submission: [pending note](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev13-installed.md). Submission is not application.


Dev14 submission: [pending overview note](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev14-collection-logos.md); not assumed applied.



Dev14 installation [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev14-installed.md); not assumed applied.


Dev15 [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev15-logo-loading.md); not assumed applied.


Dev15 installation [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev15-installed.md); not assumed applied.


Dev16 [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev16-search.md); not assumed applied.


Dev16 installation [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev16-installed.md); not assumed applied.



Dev17 [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev17-search-spacing.md); not assumed applied.


Dev17 installation [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev17-installed.md); not assumed applied.


Dev18 [pending overview submission](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260912-jellyfin-dev18-search-artwork.md); not assumed applied.


## Loose-document inventory — 23 September 2026

Inventoried root-level loose documents and same-named standalone Kotlin files against the workspace organization scheme and active project text references. Manifest: [`00_Project/LOOSE-DOCUMENTS-MANIFEST.csv`](../../00_Project/LOOSE-DOCUMENTS-MANIFEST.csv). No moves were executed: the two historical PR5743 records have explicit root-path dependencies and are to be preserved, while the standalone agent prompt and four root Kotlin files have no sufficiently clear destination or provenance. Active references were searched across project text; archived APK inspection snapshots also retain historical root paths. Review candidates remain in place. Before-copies and scope are recorded in [`backups/20260923-215248-loose-document-inventory/README.md`](../../backups/20260923-215248-loose-document-inventory/README.md). This documentation inventory did not change source, APK, release or runtime state.

## Upstream synchronization — work to be performed (23 September 2026)

**Status: planned; no synchronization performed.** A fetch-time Git inspection found branch `experiment/mobile-inspired-tv-v12` at `5abaa15745afe5016f2ae12f88845794ffe5ff54`, with 39 modified tracked files and 23 untracked entries. The branch has no upstream configured. It is 10 commits ahead and 453 behind `origin/master`; local `master` is 135 commits behind `origin/master`. The observed `origin/master` tip was `4bce7217b1d994cfb7be5676067161e2a7accf8b` (2026-09-22). This snapshot is temporal and should be refreshed immediately before integration.

**Required next steps:** (1) inventory/review and preserve all modified and untracked work in a recoverable checkpoint or reviewed WIP commit; (2) choose whether to update local `master` by fast-forward or bring the experiment forward on a new integration branch; (3) because the experiment diverges and is substantially behind upstream, plan a rebase/cherry-pick or other reviewed integration, resolving conflicts without discarding local changes; (4) run relevant build and automated checks after integration, and record results before any physical device qualification. Do not use a plain pull as the integration plan. No checkout, merge, rebase, commit, push, build, or device action was performed during this status/documentation task.

Pending shared overview submission: [upstream synchronization plan](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260923-jellyfin-upstream-sync-plan.md); not assumed applied. Documentation before-copy and restore scope: [checkpoint README](../../backups/20260923-223526-upstream-sync-plan-docs/README.md).

## Git cleanup baseline — 23 September 2026

Started the planned cleanup with a recoverable source checkpoint and review sequence: [Git cleanup plan](../../00_Project/GIT-CLEANUP-PLAN-20260923.md). The checkout remained on `experiment/mobile-inspired-tv-v12` at `5abaa15745afe5016f2ae12f88845794ffe5ff54`, with 39 modified tracked files and 30 untracked files. The NAS checkpoint at `S:\Homelab-Projects\01_Jellyfin-APK\20260923-225216-git-cleanup-baseline` contains a verified full-history bundle, tracked patch, exact-byte working-file copies, untracked files, status, and hash manifest. All 69 copied working files matched their source hashes. `git diff --check` and reverse patch applicability passed; a clean-checkout restore has not been tested. The full source remains dirty and unmodified by Git operations; no stage/commit/branch movement, build, APK/device, or server action occurred.

Next: review source/assets/tests and documentation into commit groups, verify ownership and private-data exclusions, then perform build/tests before reviewed commits. Recheck upstream divergence before any isolated integration. V8.2 remains the latest retained release; the recorded dev27 installations are unchanged. The baseline is a source recovery point, not build or physical acceptance. Documentation before-files: [local checkpoint](../../backups/20260923-230000-git-cleanup-docs/README.md). Shared chronology: [pending overview proposal](../../../Garro%20Homelab%20project/Knowledge/OVERVIEW-UPDATES/20260923-jellyfin-git-cleanup-baseline.md); submission does not imply application.

## Shared Git workspace hygiene policy integration — 26 September 2026

Reviewed portfolio Git workspace hygiene policy at `J:\My Drive\Garro Homelab project\Knowledge\GIT-WORKSPACE-HYGIENE.md` (effective 2026-09-26). Gary authorized fast-forward merge of the documentation link branch and worktree retirement.

Merged `docs/shared-git-workspace-hygiene` (commit `e8b3c78f2ce287814508f4bd80aac08bb3e927d5`, adding policy link to `source/AGENTS.md`) into `experiment/mobile-inspired-tv-v12` via `--ff-only`. Retired linked worktree `E:\Git\jellyfin-androidtv-workspace-policy` cleanly without force after verifying push to `fork/docs/shared-git-workspace-hygiene`. Retained local and remote branch refs.

Future enhancement backlog noted per Gary's direction: redesign/update artwork and behavior of top row folders (Music, Collections, Movies, Music).

```text
Git workspace observation time: 2026-09-26T12:50:48+01:00
Repository/worktree: J:\My Drive\Jellyfin apk\source
Branch or detached state and HEAD: experiment/mobile-inspired-tv-v12 at e8b3c78f2ce287814508f4bd80aac08bb3e927d5
Upstream and ahead/behind: no upstream configured; behind origin/master 453, ahead 11
Tracked/untracked/relevant ignored state: working tree clean (git status --porcelain=v1 -uall empty); ignored build outputs/local.properties intact
Local commits, unpublished work, or attached review: e8b3c78f2 published to fork/docs/shared-git-workspace-hygiene (PR pending https://github.com/Smotheredhope80/jellyfin-androidtv/pull/new/docs/shared-git-workspace-hygiene); experiment branch unpushed
Tests and qualification limits: documentation-only fast-forward merge; no APK build, unit test, or physical Shield test rerun
Deployment/runtime/physical acceptance state: Dev27 remains installed and active on Gary's and Cindy's Shields
Disposition: active
Next owner and action: Gary / next agent to plan top row folder artwork/behavior updates (Music, Collections, Movies, Music) and upstream integration
```

Documentation before-copy: `backups/20260926-125100-git-hygiene-merge/HANDOFF.md.before.txt`.
## Project 01 E: reclamation dependency review — 28 September 2026

**Result: NOT YET CLEARED.** This is a read-only classification of Jellyfin documentation-checkpoint dependencies for Project 10. No E: path, task, report, runtime resource, config, or backup was changed. `E:\Git\garro-homelab` was accessible. The E: pilot JSON is **active operational configuration** (a fixed 17-file Project 01 document scope plus host, destination and local state paths), not temporary output or an obsolete remnant. Its SHA256 at review was `757D0F487CB606AA2A60D85D1A3B3FC16174A90EF89B891B905205A2B2F896C9`. It is untracked in the shared Git checkout. The Project 01 documents at `J:\My Drive\Jellyfin apk` are authoritative source; the JSON itself has no verified alternate operational copy. The NAS checkpoints are captured document copies, not copies of the JSON or a full app/source recovery point.

| E: Path | Purpose | Status | Known Callers | Authoritative Copy | Recovery Location | Proposed Disposition | Safe to Remove Now? |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `E:\Git\garro-homelab\config-templates\backup\jellyfin-documents-pilot.json` | Host/principal, `J:` source, NAS destination, `%LOCALAPPDATA%` state, missed-run threshold and fixed 17-file scope | Active | `GARRO Jellyfin Documentation Checkpoint Pilot` passes this exact path; `run_document_checkpoint.py` reads it; registration/preview scripts derive it | Current E: original; Project 01 owns document scope, Project 10 owns task interface | No exact independent JSON copy verified; Project 10 source/recovery checkpoint may contain older bytes, unverified for this generation | KEEP UNTIL MIGRATED | No |
| `E:\Git\garro-homelab\scripts\backup\run_document_checkpoint.py`; `checkpoint_documents.py` | Scheduled producer and reusable hash-planned copier/verifier | Active | Pilot task runs first script; it imports second; Backup Freshness invokes second as verifier | E: shared implementation source, Project 10 operational stewardship | No exact alternate deployed copy verified; dated Project 10 before-file evidence is not current recovery proof | KEEP ACTIVE | No |
| `E:\Git\garro-homelab\scripts\run_silent_wait.vbs` | Synchronous hidden task wrapper carrying child exit status | Active | Pilot and Backup Freshness task actions; other Project 10 tasks also use it | E: shared wrapper source | No exact alternate copy verified in this review | KEEP ACTIVE | No |
| `E:\Git\garro-homelab\scripts\backup\Test-HomelabBackupFreshness.ps1` | Hourly checkpoint health reader; loads canonical local outcome, task result, last-success projection and verifies NAS manifest/files | Active | `GARRO Homelab Backup Freshness Check` task; Toolbox consumes resulting report | E: Project 10 monitoring source | No exact alternate deployed copy verified | KEEP ACTIVE | No |
| `E:\Git\garro-homelab\reports\backup\` | Hourly backup-health reports, including Jellyfin checkpoint status; historical observations are unique | Recovery/checkpoint | Backup Freshness task writes; source Toolbox reads; installed Toolbox has separate package reports | E: Project 10 operational report history | No exact full-history copy verified; fresh snapshots are reproducible, past observations are not | KEEP UNTIL MIGRATED | No |
| `E:\Git\garro-homelab\scripts\backup\Register-DocumentCheckpointPilot.ps1`; `Preview-DocumentCheckpointTask.ps1`; `README.md` | Registration/preview procedure and operator documentation, including the JSON path | Archive/reference and maintained source | Manual Project 10 installation/preview; not invoked by the daily task | E: shared repository source | No exact current copy verified | KEEP ACTIVE | No |

**Observed call chain and impact.** Task Scheduler readback on 28 September shows `GARRO Jellyfin Documentation Checkpoint Pilot` enabled/Ready, daily 02:30, invoking `wscript.exe` with E: `scripts\run_silent_wait.vbs`, `C:\Python313\python.exe`, E: `scripts\backup\run_document_checkpoint.py`, and the exact E: JSON. Last run 28 September 02:30 returned 1; next scheduled run 29 September 02:30. Removing the JSON or either script/wrapper would make subsequent capture fail before creating a new checkpoint. The hourly `GARRO Homelab Backup Freshness Check` invokes the E: wrapper and E: `Test-HomelabBackupFreshness.ps1`, writing `E:\Git\garro-homelab\reports\backup`; at 15:00 on 28 September it returned 1. Its script defaults to E: `checkpoint_documents.py` and local `C:\Users\garya\AppData\Local\garro-homelab\checkpoint-documents` for the producer outcome. Removing those E: inputs breaks monitoring or verification; deleting old reports loses dated evidence. The source Toolbox reads the backup report; the installed Toolbox uses separate package-local reports. No Jellyfin Shield APK runtime, Docker bind mount or media-server config caller of the pilot JSON was identified in the bounded source/task review; NAS/server-side and all manual/external consumers were not exhaustively audited.

**Checkpoint state and recovery.** Canonical `C:\Users\garya\AppData\Local\garro-homelab\checkpoint-documents\outcome.json` reported 28 September 02:30 failed: `Pilot file set changed; review scope before capture`; `last-attempt.json` agrees. `last-success.json` names `\\192.168.68.73\Backups\Homelab-Projects\01_Jellyfin-APK\checkpoint-20260923-013002-operator-51b2aeb3`, 17 files, last successful 23 September 02:30. The NAS path and manifest were readable in this review. Project 10's 26 September evidence says its verifier matched 17/17 files; a fresh isolated restore was NOT RUN here. Project 10 identified the additional current source document `00_Project/GIT-CLEANUP-PLAN-20260923.md`; Project 01 has not accepted a new fixed scope. Local outcome/projections are operational audit state, not independent backup payloads. `run.lock` and atomic temporary JSONs are regenerable mechanics, but this does not make the canonical outcome or saved history disposable. The NAS checkpoints omit APKs, source code, signing material, databases, CSV indexes and runtime configuration.

**Cross-project dependencies.** Project 01 owns document selection and checkpoint content; Project 10 owns the scheduled task, runner/wrapper, state/health interface and reports. Project 10's Backup Freshness checker references the exact local outcome path and E: verifier; the pilot task references the E: JSON and runner. Any future movement of Project 01 scope to its project folder needs coordinated task-argument, registration/preview, checker, state and report-reader review plus a successful natural run and checkpoint verification. The shared generic copier can remain Git source. Project 02 Jellyfin server and media automation were outside this pilot; their use of E: was not proven absent by this review.

**Unresolved dependencies:** Project 01 must decide whether the eighteenth document enters the fixed scope or whether the pilot is redesigned; Project 10 must coordinate any config/task/state migration and verify the next natural run and health transition. Exact recovery copies of the current untracked JSON and runner/wrapper were not established. Indirect/manual, NAS container, server and other-project consumers require owner review before broad E: removal. **Removal candidates:** none of the listed E: originals. Fresh report snapshots and bytecode can be regenerated in principle, while report history and active task inputs must be retained. Project 10's central manifest remains unchanged by this review. This Project 01 handoff is the owning disposition; [Project 10 manifest](<../../../Homelab Reliability/00_Project/ACTIVE-TASKS/GARRO-HOMELAB-RECLAMATION-MANIFEST.md>) and [ignored-files review](<../../../Homelab Reliability/00_Project/ACTIVE-TASKS/20260928-ignored-files-operational-dependency-review.md>) hold the wider checkout findings. Targeted handoff before-copy: `../../backups/20260928-153000-e-reclamation-review-before/HANDOFF.before.md`; restore only this file after comparing newer work. Source/physical Shield tests were NOT RUN; no APK/release identity changed.

## Project 01 checkpoint configuration decision — 28 September 2026

**Operational configuration location: SEPARATE FROM PUBLIC GIT. Final disposition: MIGRATION REQUIRED — MOVE OPERATIONAL CONFIG OUT OF GIT.** This decision supersedes the preceding review's open owner-scope question; it does not change the installed pilot. The current untracked `E:\Git\garro-homelab\config-templates\backup\jellyfin-documents-pilot.json` is a live BEDROOM-PC task input containing an account, source path, NAS address, local state path and an owner-specific document list. It should not become authoritative in a repository intended for public publication. Untracked status does not isolate it from the working tree, accidental staging or checkout loss. Project 01 chooses **proposed** authoritative operational path `J:\My Drive\Jellyfin apk\00_Project\Operational-Config\jellyfin-documents-pilot.json`, outside the `source` Git checkout and the public `garro-homelab` repository. The proposed folder/file does not yet exist; the E: original remains the active authority until an accepted migration. This JSON currently contains no credential but has host-specific topology; do not add secrets to the synced Project 01 workspace. A later secret-bearing requirement needs a separately selected protected local store.

**17/18 scope decision and current 22-file readback.** Include `00_Project/GIT-CLEANUP-PLAN-20260923.md` (SHA256 `88CE724862DB2BDD8A7B7998C39EC2324B1561062CB07066772489608F7ACFB4`) in this *documentation* pilot. It is a maintained, linked Git recovery plan in the existing `00_Project` selection, not build output or a temporary file. A fresh read-only call to the actual copier's `plan` on 28 September discovered **22** eligible files. In addition to the configured 17, it found this plan and four maintained Project 01 task records: `00_Project/ACTIVE-TASKS/20260927-category-navigation.md`, `20260927-cindy-v9-install.md`, `20260927-no-delete-youtube.md`, and `20260927-v9-publication.md`. Those four are project task/acceptance records within the existing recursive `00_Project` purpose boundary and belong in the document checkpoint too. This **22-name observed set** is Project 01's intended scope for this review, subject to a fresh exact-name/content review immediately before migration; the older 18-file statement was a dated intermediate finding. The authoritative rule is root `.md`/`.txt` and recursively `00_Project` `.md`/`.txt`, with regular-file/path and size guards, plus an owner-reviewed explicit manifest of permitted names. Discovery of a new or missing name must continue to fail closed for Project 01 review; a count alone is never authority. The current copier discovers that boundary and the runner compares the name set, so an owner-reviewed addition to `Files` is the narrow proposed correction. It is **not** permission to run or edit the active task now. This pilot still omits `source/docs` (including this canonical handoff), APKs, app source, signing material, databases and runtime configuration; their recovery coverage is separate and is not established by a 22-file document capture. If any newly discovered document contains protected information, review/exclude it before capture rather than automatically broadening the selection.

**Git representation.** Keep the generic `scripts/backup/checkpoint_documents.py`, `run_document_checkpoint.py`, tests and task-wrapper source under Project 10's Git stewardship. Retain only a sanitized generic `config-templates/backup/document-checkpoint.example.json` or schema, with placeholder host/account/source/destination/state values and no Project 01 file names or private paths; it is illustrative, never a task argument. Sanitize `scripts/backup/README.md` and registration/preview examples before public publication so they describe how an operator supplies a project-local config without embedding BEDROOM-PC identity, NAS address or the old E: JSON path. Exclude the active JSON from Git staging/publication. No example, schema or source edit was made in this decision review.

| Caller / Component | Current Reference | Required Change | Owner |
| --- | --- | --- | --- |
| `GARRO Jellyfin Documentation Checkpoint Pilot` Windows task | Exact E: JSON argument, E: wrapper and E: runner; daily 02:30 | After verified project config exists, change only JSON argument through a controlled task update; preserve principal, schedule, wrapper, runner and rollback definition; read back action | Project 10 scheduler; Project 01 approves scope |
| `scripts/backup/Register-DocumentCheckpointPilot.ps1` and `Preview-DocumentCheckpointTask.ps1` | Derive `..\..\config-templates\backup\jellyfin-documents-pilot.json` | Accept/read the approved project-local config path without treating a public example as operational; update guarded procedure and test/readback | Project 10 |
| `scripts/backup/run_document_checkpoint.py` | Reads task-supplied JSON; imports sibling `checkpoint_documents.py` | No source-path change inherently required; verify the new argument, host/principal, source/destination, state and Project 01-approved implementation-time name set (22 names observed in this review) before scheduling | Project 10 implementation; Project 01 scope |
| `scripts/backup/checkpoint_documents.py` | Runner imports it; Backup Freshness defaults to its E: verifier path | Keep generic copier/verifier source; verify discovered names and completed checkpoint under the approved scope | Project 10 |
| `scripts/run_silent_wait.vbs` | Task wrapper passes the E: runner and JSON argument | Keep wrapper source and synchronous exit behavior; task-action readback must prove the new argument passes through | Project 10 |
| `scripts/backup/Test-HomelabBackupFreshness.ps1` and `GARRO Homelab Backup Freshness Check` | Canonical state under `C:\Users\garya\AppData\Local\garro-homelab\checkpoint-documents`; E: verifier; E: report output | No state-path change is required solely to move JSON; verify checker still reads the same canonical outcome and correct new checkpoint, and change any future path-bound references as a coordinated Project 10 step | Project 10 |
| Toolbox/source report reader and `E:\Git\garro-homelab\reports\backup` | Reads Project 10's saved monitoring result | Preserve report continuity and verify new healthy/failure status reaches intended reader; separate report migration remains a Project 10 decision | Project 10 |
| Project 01 backup plan, handoff and Project 10 backup README/runbook | Refer to pilot scope and/or old E: config path | Document approved scope, config authority, operator recovery and exact deployed task action; replace stale old-path instructions after cutover | Project 01 for its records; Project 10 for its records |
| Manual/external or other-project callers | No additional exact caller established by bounded review | Search relevant task/script/runbook/automation/Compose/media tooling for the old exact path before retirement; resolve any hit with its owner | Project 10 coordination; consuming owner |

**Recovery requirement.** Before cutover, preserve the approved operational JSON (including the implementation-time reviewed name list; 22 names observed now) as an exact-byte, SHA256-verified, dated Project 01 configuration recovery copy under `\\192.168.68.73\Backups\Homelab-Projects\01_Jellyfin-APK\` with a manifest, restore instructions and Project 01 backup-index entry. Verify retrieval/readback and a restore to a separate temporary location; do not claim independent/off-NAS recovery from a NAS-only copy or Google Drive sync. Keep local state/outcome history and document checkpoint payloads separate. The existing 17-file checkpoint does not contain this JSON.

**Migration acceptance gates.** Project 10 may plan and coordinate the change, but **may not make Jellyfin scope/content decisions on behalf of Project 01**. Before retiring the E: JSON: (1) Project 01 approves the exact implementation-time name set and hash-bound new JSON at the proposed path; (2) its dated recovery copy and separate-location restore/hash readback pass; (3) task, registration/preview, runbook and any other confirmed caller use the new path, with task definition and rollback readback; (4) public Git contains only reviewed generic source, sanitized documentation and example/schema; (5) a normal scheduled 02:30 run, not merely a forced invocation, returns success and writes a new checkpoint covering that approved set (22 names observed on this review); (6) the independent verifier matches manifest, count and file hashes; (7) canonical outcome and Project 10 Backup Freshness/Toolbox status reflect that run without stale-path errors; and (8) a fresh old-path search plus actual task readback finds no required consumer. Only then is the E: JSON **eligible** for separately authorized retirement. Until then it remains active and must not be moved, deleted or overwritten.

Decision-only verification: reviewed the live JSON, Scheduled Task argument, runner selection code, 23 September cleanup plan, four task records, current canonical outcome and a read-only 22-file copier plan; no task, E: file, E: Git content, runtime, monitoring setting or APK was changed. Only this Project 01 handoff and its local before-file index were updated. Scheduled recovery acceptance and physical Shield tests were **NOT RUN**. Targeted before-files and restore note: `../../backups/20260928-154500-checkpoint-config-decision-before/README.md`. Next owners: Project 01 confirms the exact implementation-time file generation; Project 10 proposes and qualifies the coordinated migration under its own operational controls.
