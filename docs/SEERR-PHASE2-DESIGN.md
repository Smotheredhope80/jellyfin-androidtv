# Seerr phase 2 — richer details and advanced requests — design note

**Status:** DESIGN, awaiting Gary's approval. 27 October 2026. No code yet.
**Author:** BEDROOM-PC / Claude Code session `61ef7ec8-b139-4b08-8d24-82703bfdfcc2`.
**Builds on:** [phase 1](SEERR-DISCOVER-DESIGN.md) (dev32 r2, `e63e1d1df`, T05 PASS 27 Oct).
**Target:** dev33 candidate on `experiment/mobile-inspired-tv-v12`.

## Goal

Gary asked for the Seerr details page to match what SeerrTV (`devmesh-git/seerrtv`) offers:

1. **Trailers** on the Seerr details page.
2. **Ratings**: Rotten Tomatoes, IMDb and TMDB.
3. **Fuller details**: box office (revenue), budget, genres, certification, studios or networks, director or creators, and cast names.
4. **Advanced requests**: choose the server, quality profile, root folder and tags when requesting.

## Sources and licence

- SeerrTV is MIT-licensed ("Copyright (c) 2024 DevMesh (original author)" and "Copyright (c) 2024 Seerr Community"). MIT code may be adapted into this GPL-2.0 fork as long as the notice is kept.
- **This design did not open SeerrTV's code**, and it doesn't need to. Every API fact below comes from Seerr itself (`seerr-team/seerr`, also MIT):
  - the `seerr-api.yml` OpenAPI file on `develop`;
  - the server source at tag **`v3.5.1`**, which matches Gary's server: `server/entity/MediaRequest.ts`, `server/routes/service.ts`, `server/routes/movie.ts` and `server/models/{Movie,Tv,common}.ts`;
  - `server/lib/permissions.ts` and `server/routes/index.ts`, read on `develop` only.
- If SeerrTV code is ever adapted, the file gets an MIT notice header and `docs/` gets a third-party notice entry. The JellyWatch clean-room rule from phase 1 still applies.

## Verified API facts (Seerr v3.5.1 source and read-only probe, 27 Oct)

The unauthenticated probe shows `GET /status` → `3.5.1`, with `updateAvailable` false. `/settings/public` is unchanged from 26 Oct: `movie4kEnabled` and `series4kEnabled` are both false, `partialRequestsEnabled` is true, `region` is empty and `locale` is `en`.

| Need | Endpoint / field | Notes |
| :-- | :-- | :-- |
| Trailers | `MovieDetails.relatedVideos[]`, `TvDetails.relatedVideos[]` | `{site:"YouTube", key, name, size, type}` where `type` is one of Trailer, Teaser, Clip, Featurette and others. It is already in the `/movie/{id}` and `/tv/{id}` response, so no extra call is needed. |
| RT + IMDb (movie) | `GET /movie/{id}/ratingscombined` | `{rt:{criticsScore, criticsRating, audienceScore, audienceRating}, imdb:{criticsScore}}`. Either part may be absent. |
| RT (TV) | `GET /tv/{id}/ratings` | `{criticsScore, criticsRating}` only. There is no IMDb score for TV. |
| TMDB score | `voteAverage`, `voteCount` | Already in the details response. |
| Box office | `MovieDetails.revenue`, `budget` | TMDB figures in USD. `0` means unknown. |
| Other details | Movie: `tagline`, `genres`, `releases.results[].release_dates[].certification`, `productionCompanies`, `credits.crew` (Director), `credits.cast`, `status`, `originalLanguage`. TV: `tagline`, `genres`, `contentRatings.results[]`, `networks`, `createdBy`, `episodeRunTime`, `lastAirDate`, `nextEpisodeToAir`, `status`, `keywords` | All are in the details response. |
| Server list | `GET /service/radarr`, `GET /service/sonarr` | Any signed-in user can call these (`isAuthenticated()` with no permission). Each entry is `{id, name, is4k, isDefault, activeProfileId, activeDirectory, activeTags[]}`. Sonarr entries add `activeAnimeProfileId`, `activeAnimeDirectory` and `activeLanguageProfileId`. |
| Profiles, folders, tags | `GET /service/radarr/{id}`, `GET /service/sonarr/{id}` | `{server, profiles[{id,name}], rootFolders[{id,path,freeSpace,totalSpace}], tags[{id,label}], languageProfiles?}`. The OpenAPI file lists only `profiles`; the source returns everything shown here. The server asks Radarr/Sonarr live, so a downed Arr gives 500. |
| Request body | `POST /request` | Accepts `serverId`, `profileId`, `rootFolder`, `languageProfileId` and `tags:number[]`. `tags` is not in the OpenAPI file but is read by `MediaRequest.ts`. |
| Permission | `GET /auth/me` → `permissions` (bitmask) | Advanced means `ADMIN (2)`, `MANAGE_REQUESTS (16)` or `REQUEST_ADVANCED (8192)`. |

### Key finding: the client must enforce the advanced-request permission

In v3.5.1, `MediaRequest.request()` **does not reject** `serverId`, `profileId`, `rootFolder` or `tags` from a user without the advanced permission. It uses the permission only to decide whether override rules may replace the chosen values. Seerr's web UI simply hides the options from those users.

So if this app showed the options to everyone, it would quietly get around the restriction Gary or P06 set on the server. **The app therefore shows the options and sends these fields only when `/auth/me` reports an advanced permission.** Every other user keeps the phase 1 behaviour exactly.

## Design

### 1. Trailer button

- **Choosing the trailer:** keep only `relatedVideos` entries with `site == "YouTube"` and a valid 11-character key (the existing `getYoutubeVideoId` pattern). Prefer type Trailer, then Teaser, then any other type. Within a type, keep the server's order, which is TMDB's.
- A **Trailer** button sits after Open in Jellyfin and Request in the details button row. The first button still gets the initial focus. Nothing autoplays, so the AGENTS.md rule that preview content must never autoplay holds.
- **Playback:** the button reuses the existing path. `TrailerUtils` gets one small public helper, `getYoutubeTrailerIntent(context, videoKey)`, which wraps the private `getTrailerIntent`. That opens `EmbeddedTrailerActivity` with the standard YouTube player, with the external YouTube fallback unchanged. No new player code is added.
- The button is hidden when no YouTube video exists.

### 2. Ratings

- Ratings load **in parallel** with the details call (`async` in the ViewModel) and never hold up the page. On failure, Timber logs a warning and the chips are left out. There is no error toast.
- They show as a text line under the metadata line, for example: `Rotten Tomatoes 85% · Audience 65% · IMDb 6.5 · TMDB 7.2`.
  - The design uses text labels only, with no RT or IMDb logos, which are trademarked artwork.
  - "Certified Fresh" and "Fresh" are not drawn as icons. The critics rating word may appear in the caption.
- TMDB shows only when `voteCount > 0`. RT and IMDb show only when their score is present.

### 3. Fuller details

The single "header" item in the details `LazyColumn` becomes a header plus an **info section**. Each line below is shown only when it has data:

| Movie | TV |
| :-- | :-- |
| Tagline (italic, under the title) | Tagline |
| Certification · Genres (up to 3) | Content rating · Genres (up to 3) |
| Release date · Runtime · Status | First aired–last aired · Episode runtime · Status (`Returning Series`, `Ended`) |
| **Box office** `$1.2B` · Budget `$200M` (compact USD) | Networks (up to 2) · Next episode date |
| Director · Studios (up to 2) | Created by (up to 2) |
| Cast: the first 8 names in one line | Cast: the first 8 names |

- **Certification region.** Seerr's `region` setting is empty, so the device's locale country (`Locale.getDefault().country`) is used, falling back to `US`. For movies, the first non-blank theatrical certification wins, then any non-blank one.
- **Money.** The app's minSdk is 23 (`gradle/libs.versions.toml`), so it can't use `NumberFormat.getCompactNumberInstance`, which needs API 30. Instead, a small unit-tested formatter, `formatUsdCompact`, writes values like `$950K`, `$200M` or `$1.2B`, and returns null for `0` or `null`. TMDB stores these as USD, so the figures are labelled USD and never converted.
- Cast and crew are plain text in phase 2, with no person pages (see Out of scope).
- The info section isn't focusable. Long details scroll with the column as Down moves focus to the season list, as today. On a movie with no season list, every line must fit on one 1080p screen. Tagline and overview keep their `maxLines`, and the info rows are limited to one line each, so no unreachable text sits below focus.

### 4. Advanced request options

**Permission check.** The repository reads `GET /auth/me` once per session and keeps `permissions` in memory only. It is read again after sign in and when the details screen opens, if it is older than 10 minutes. It is never persisted. `canRequestAdvanced = perms & (2 | 16 | 8192) != 0`.

**Flow for advanced users.** **Request** opens a **request options panel**, a full-screen overlay inside the details fragment. Back closes it and keeps the season selection. For non-advanced users, Request sends the phase 1 body as today.

**Panel rows**, each a `ListButton`. Pressing one opens a simple choice list, and Back returns to the panel:

| Row | Source | Default | Notes |
| :-- | :-- | :-- | :-- |
| Server | `/service/radarr` or `/service/sonarr`, **filtered to `is4k == false`**, since 4K is off on Gary's server | The `isDefault` server, else the first | Hidden when only one server exists |
| Quality profile | `/service/{arr}/{serverId}` → `profiles` | `activeProfileId` (anime: `activeAnimeProfileId`) | |
| Root folder | `rootFolders` | `activeDirectory` (anime: `activeAnimeDirectory`) | The caption shows free space, for example "1.2 TB free" |
| Tags | `tags` (multi-select checklist) | `activeTags` (anime: `activeAnimeTags`) | "None" when empty |
| Language profile | `languageProfiles` | `activeLanguageProfileId` | Sonarr v3 only. Hidden when it is null, which is the normal case for Sonarr v4 |
| **Request** button | | | Shows the season count for TV, as today |

**Anime defaults (TV).** A show counts as anime when its `keywords` contain TMDB keyword id `210024` ("anime"), which is what Seerr's own web UI checks. The anime defaults are used only if the server defines them.

**What is sent.**
- `serverId` is sent only when the chosen server is not the default.
- Profile, root folder, tags and language profile are sent **only when the user changed them** from the shown default. Untouched values are omitted, so Seerr's override rules and server defaults still apply, as they do in phase 1. An advanced user who presses Request without changing anything sends the phase 1 body exactly.
- `is4k`, `userId` and `ignoreQuota` are still never sent.

**Failures.**
- If the service list or server details fail (a downed Arr gives 500), the panel shows the server's message and a single **Request with defaults** button that sends the phase 1 body.
- If the user changes the server and that server's details fail, the panel falls back to the default server and says so.

**After the request**, behaviour is unchanged: the same outcome toasts, then a reload.

**To verify in T06:** when `serverId` is a non-default server and `profileId` and `rootFolder` are omitted, Seerr should apply *that* server's defaults. If it doesn't, the client will instead send the chosen server's defaults explicitly whenever the server is changed.

### Code shape

| Piece | Change |
| :-- | :-- |
| `data/seerr/SeerrModels.kt` | Add optional wire fields: videos, genres, revenue/budget, tagline, credits, companies/networks, releases/contentRatings, keywords and `voteAverage/Count`. Add ratings models, service models (`SeerrServiceServer`, `SeerrServiceDetails`, profile/folder/tag/language) and `SeerrUser.permissions`. |
| `data/seerr/SeerrMapping.kt` | Pure, unit-testable functions: `pickTrailer`, `certificationFor(region)`, `formatUsdCompact`, `isAnime`, `canRequestAdvanced`, `defaultRequestOptions(server, details, anime)` and `seerrRequestBody(…, options, defaults)`, which sends changed fields only. `SeerrTitle` gains `info`, `trailerKey`, `keywords`. A new `SeerrRatings` UI model. |
| `data/seerr/SeerrRepository.kt` | Add `ratings(type, id)`, `me()` (permissions, kept in memory), `services(type)` and `serviceDetails(type, serverId)`. Service details are cached per server for the repository's lifetime, with a 5-minute expiry. `request(…)` takes optional options. |
| `ui/seerr/SeerrDetailsViewModel.kt` | Parallel ratings load, permission state, options panel state (open, loading, error, selections) and a fallback request. |
| `ui/seerr/SeerrDetailsFragment.kt` | Trailer button, ratings line, info section and options overlay. The overlay is a new private composable file if the fragment grows past about 450 lines (`SeerrRequestOptions.kt`). |
| `util/sdk/TrailerUtils.kt` | One public helper, `getYoutubeTrailerIntent(context, key)`. Existing behaviour is unchanged. |
| `res/values/strings.xml` | New strings: trailer, ratings labels, box office/budget, options rows, "Request with defaults", free space. |

No change is needed to the manifest, network security config, preferences, backup rules or toolbar. No new dependency is needed.

## Verification plan

- **Unit tests** use hand-written fixtures with no copied personal server data. They cover:
  - trailer pick order;
  - certification region fallback;
  - USD compact formatting, including `0` and `null`;
  - anime detection;
  - the permission bitmask for admin, manage, advanced and plain users;
  - default selection, including anime and missing defaults;
  - the request body: unchanged options give exactly the phase 1 body, while changed profile, folder, tags or server give only those fields;
  - service JSON parsing, including the extra fields not in the spec and `languageProfiles: null`;
  - combined-ratings parsing with `rt` or `imdb` missing.
- **Lint**, compared with the dev32 baseline.
- **Tier 3 review:** this changes the outgoing request body and the permission-dependent UI, so `garro-shield-verifier` reviews the dev33 source and artifact before install.
- **New physical case T06**, to add to `SHIELD-TEST-CATALOG.md` with the dev33 candidate:
  1. Open a movie: ratings line, info section and box office show.
  2. Play its trailer, then press Back to return to details. Check that focus is not trapped and nothing autoplayed.
  3. Open a TV show: network, content rating and creators show, and there is no IMDb chip.
  4. As Gary (advanced, if granted), open the options panel. Change the profile and add a tag, then request. In Seerr's web UI, the request shows the chosen profile and tag. Gary deletes it afterwards if unwanted.
  5. Request with no changes: Seerr shows the default profile.
  6. Optional: change the server to a non-default one, to confirm the server-defaults question above.
  7. As a non-advanced user, if one is available: Request behaves exactly as in dev32, with no panel.
  8. Regression: T05 discover, search and Open in Jellyfin still pass.

## Out of scope for phase 2

Person and filmography pages, similar and recommended rows, issue reporting, request edit, cancel and approve, 4K, watchlist, Discover filters, and a home-screen row. These are candidates for phase 3. The captions toast fix (from dev31 T04) is a separate small task.

## Open questions for Gary

1. **Does your Seerr account have Advanced Requests (or admin)?** If not, the options panel won't appear for you. Granting it is a Seerr user setting on P06's server, so it is either your change in Seerr's web UI or a note to P06's `00_Project/INBOX.md`. This app won't change it.
2. Is it OK to show **box office and budget in USD** (TMDB's currency), without conversion?
3. Should anything from Out of scope come into phase 2? The likely candidates are cast names you can open, or a "Similar titles" row.
