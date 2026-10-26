# Seerr discover and request — design note

**Status:** DESIGN, 26 October 2026. No code yet. Gary assigned this in chat after dev31 T04 passed.
**Author:** BEDROOM-PC / Claude Code session `4e325f0d-f571-4791-9906-b0faecac5111`.

## Goal

Let Gary browse what's trending or popular, search for a film or show that isn't in Jellyfin, and request it through Seerr. All of this happens with the Shield remote, without leaving the app. An item that is already available opens its normal Jellyfin details page.

## Clean-room boundary

JellyWatch (`JellyWatchteam/JellyWatch`) is "All rights reserved". None of its code, layouts, strings or assets may be copied, translated or paraphrased into this GPL-2.0 fork. For this design, its source was not opened. This design comes only from:

- Seerr's public OpenAPI file (`seerr-team/seerr`, `seerr-api.yml` on `develop`, `info.version 1.0.0`), used for endpoint names and fields;
- read-only, unauthenticated probes of Gary's server on 26 Oct;
- this fork's existing code patterns.

Any future work keeps the same rule.

## Server facts (read-only probe, 26 Oct 2026)

`GET http://192.168.68.73:5055/api/v1/status` reports version `3.5.1`, `updateAvailable:false`. `GET /api/v1/settings/public` reports:

| Setting | Value | Meaning |
| :-- | :-- | :-- |
| `mediaServerType` | `2` | Jellyfin is the media server |
| `mediaServerLogin` | true | Jellyfin users can sign in |
| `localLogin` | true | |
| `movie4kEnabled` / `series4kEnabled` | false | No 4K requests; omit `is4k` |
| `partialRequestsEnabled` | true | Individual TV seasons can be requested |
| `enableSpecialEpisodes` | false | Hide season 0 |

P06 owns the server. This feature needs no Seerr settings change. If a change is ever needed, it goes to P06's `00_Project/INBOX.md`.

## Authentication — the key decision

`POST /api/v1/auth/jellyfin` takes `username` and `password` (`hostname` is only needed during Seerr's first setup) and returns a `connect.sid` session cookie. The TV app only holds a Jellyfin **access token**, not the password, so it can't sign in silently.

| Option | Verdict |
| :-- | :-- |
| **A. One-time Seerr sign-in on the TV** (Jellyfin username prefilled, password typed once, only the cookie kept) | **Chosen.** Acts as Gary's own Seerr user, so Seerr's permissions and quotas apply. |
| B. Seerr admin `X-Api-Key` stored in the app | Rejected. A full admin credential on a TV device bypasses per-user permissions. |
| C. Seerr local (email) account | Possible fallback, same flow as A against `/auth/local`. Not needed while Jellyfin login is on. |

Rules for option A:

- The password is never stored or logged. It lives only in the sign-in dialog's state until the request returns.
- Only the `connect.sid` cookie and the Seerr base URL are stored, in app-private preferences keyed to the current Jellyfin user. Sign out calls `POST /auth/logout` and deletes the cookie.
- Any 401/403 clears the cookie and offers "Sign in to Seerr again". Silent retries are not allowed.
- Backup: `backup_content.xml` and `backup_rules.xml` back up only an allow-list of files (checked 26 Oct). The cookie therefore goes in its own SharedPreferences file (`seerr_session`), which is not on that list and so is never backed up. The Seerr URL can sit in the normal user preferences. Keep the session file off the allow-list.

## User experience (D-pad first)

1. **Entry point.** A "Discover" button in `MainToolbar`, next to Search. It appears only after Seerr is configured, so Cindy's Shield and any unconfigured user see no change.
2. **Discover screen.** Rows: Trending (`/discover/trending`), Popular movies (`/discover/movies`), Popular TV (`/discover/tv`), and Upcoming movies (`/discover/movies` with `primaryReleaseDateGte` = today). Cards are posters with a status badge: Available, Partly available, Requested/Processing, or none. Pages load lazily as focus nears the end of a row. Up from the first row reaches the toolbar, keeping the existing focus boundary rule.
3. **Search.** A Seerr search field at the top of Discover (`/search?query=`). It doesn't merge into Jellyfin search in phase 1, so the two result sources stay clearly separate.
4. **Details.** `GET /movie/{id}` or `/tv/{id}` shows the backdrop, title, year, overview, runtime/seasons and status.
   - Available: the primary button is **Open in Jellyfin**, using the Jellyfin item id from Seerr's media info. If no id is present, the button is hidden.
   - Not requested: the primary button is **Request**.
   - TV: a season checklist (season 0 hidden, already available or requested seasons disabled), with "All missing seasons" as the default.
   - Pending/approved: shows the request status read-only. No cancel in phase 1.
5. **Request.** `POST /request` with `mediaType`, `mediaId` and, for TV, `seasons`. It never sends `is4k`, `serverId`, `profileId`, `rootFolder`, `userId` or `ignoreQuota`, so Seerr's own defaults and permissions decide. Result toast: "Requested", "Waiting for approval" or the server's error message. The badge then refreshes.
6. **My requests.** A row on Discover from `GET /request?requestedBy=<me>&take=20`, where `me` comes from `GET /auth/me`.

Not in phase 1: request cancel/edit, 4K, admin approval, quota display, a home-screen row, and pushing Seerr search results into Jellyfin search. These can be added later once phase 1 passes.

## Implementation shape

| Piece | Location (proposed) | Notes |
| :-- | :-- | :-- |
| HTTP client | `data/seerr/SeerrClient.kt` | Uses OkHttp, which is already on the classpath through media3/coil; it gets declared explicitly in the version catalogue. `kotlinx.serialization` with `ignoreUnknownKeys`. Small cookie jar limited to the Seerr host. 10 s timeouts. |
| Models | `data/seerr/model/*.kt` | Only the fields used: ids, title/name, dates, poster/backdrop paths, `mediaInfo.status`, `mediaInfo.jellyfinMediaId`, seasons, request status. |
| Repository | `data/seerr/SeerrRepository.kt` | Maps raw status numbers to a Kotlin enum (media 1–6, request 1–3). Koin singleton. |
| Preferences | `preference/SeerrPreferences.kt` | Base URL, cookie, Seerr user id. Per Jellyfin user. |
| Settings | `ui/settings/screen/seerr/` | Server URL, sign in/out, connection test (`/status`). Linked from `SettingsMainScreen`. |
| Screens | `ui/seerr/` | Compose Discover and details screens, using the existing card and focus components. New destinations in `Destinations.kt`. |
| Images | Seerr's poster paths are TMDB-relative | Phase 1 loads from TMDB's public image CDN through coil, at poster `w342` and backdrop `w1280`. If Seerr's image cache proxy proves usable on 3.5.1, switch to it so the TV fetches only from the LAN. |

LAN cleartext to `192.168.68.73:5055` is already allowed: `network_security_config.xml` permits cleartext in its base config (checked 26 Oct). No manifest change is needed. The public HTTPS Seerr hostname also works, but the LAN URL is the default.

## Verification plan

- **Unit tests:** JSON parsing against hand-written fixtures (not copied server output with personal data), status mapping, the request-body builder (movie, TV subset, "all missing"), 401 handling that clears the cookie, and the toolbar button hidden when unconfigured.
- **Lint:** compared against the dev31 baseline.
- **New physical case T05** (to add to `SHIELD-TEST-CATALOG.md` with the dev32 candidate): sign in, browse rows, D-pad focus with no traps, search, open an available title in Jellyfin, request one movie and one TV season, see the request in Seerr's web UI, sign out, and a bad-password error.
- **Test hygiene:** test requests go to something Gary actually wants, or are deleted afterwards in Seerr by Gary.
- **Tier 3 review:** this adds networking and stored session credentials, so `garro-shield-verifier` reviews the dev32 source and artifact before install.

## Open questions for Gary

1. Should the Discover button appear on Cindy's Shield later, or stay Gary-only?
2. Do requests from Gary's Seerr account auto-approve? This decides whether the toast usually says "Requested" or "Waiting for approval". It can be read from `/auth/me` permissions at runtime.
