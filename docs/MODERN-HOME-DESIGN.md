# Modern native TV home design preview

## Native implementation - 12 September 2026

Gary approved Balanced and then explicitly requested implementation. The native ElegantFin home now has a 224dp non-focusable artwork/title-logo preview, metadata/format badges, synopsis and a display-only Resume timestamp. A missing/loading/failed logo falls back to the title. Image URLs use bounded dimensions and existing Coil loading; selection changes dispose the old painter. Existing library row requests include MediaStreams only for the modern home, not a per-focus details query. Badges describe available source metadata, not measured display/audio output.

Home rows use 90dp landscape cards with existing watch/progress overlays and saved remaining time. Library views use 40dp compact shortcuts. Configured section ordering and standard toolbar actions remain native; the preview does not reproduce the mockup's simulated player/details pages. Default themes keep their previous presenters and lighter queries. The accepted resume implementation is unchanged.

Source/build evidence is in ../../apk-inspection/20260912-dev7-modern-home-r2 and the current candidate status is in CURRENT-RELEASE.md. The first draft is retained separately with its superseded lint finding. Physical layout/focus/artwork/performance acceptance is pending U02 in SHIELD-TEST-CATALOG.md. Native layout is adapted to Leanback scrolling rather than a pixel-for-pixel copy of the browser preview.

Created: 12 September 2026. Owner: Jellyfin Android TV / Shield APK project.

Scope: interactive browser mockup approved as the next design step. This is not Android implementation or APK qualification. The last verified installed build remains dev6; the Shield was not queried or operated during this design pass.

## Proposed home layout

- Navy background, violet selection ring and large artwork area that follows the selected movie.
- Movie title logo, concise synopsis, runtime and format information above Continue watching.
- Landscape cards with visible progress and time remaining, followed by library shortcuts.
- Non-focusable artwork preview. Directional navigation stays on actionable cards and controls. Up from the first row goes to the toolbar; Up from library shortcuts returns to the cards. Back from details restores the selected card.
- Balanced layout is the proposed default at 1024 x 576. Cinematic and compact variants are exploratory controls, not final TV viewport specifications.

The preview supports title selection, local details, simulated Resume/Play from beginning, a five-title search and illustrative library navigation. Playback preview never starts media. Aliens 31:39 is the earlier accepted test timestamp, not current live progress. Other progress values and format badges are illustrative. Artwork was read from existing local movie artwork files; originals were unchanged.

## Evidence

Preview and supporting files are under:

`C:\Users\garya\.codex\visualizations\2026\09\12\01a0951d-d046-7d03-b32f-4b8095eae756`

- `modern-shield-home.html`: self-contained inline visualization fragment, 743,673 bytes.
- `home-artwork-sources.json`: artwork source inventory.
- `interaction-checks.json` and `check-home.cjs`: browser checks and repeatable procedure.
- `modern-home-desktop.png`, `modern-home-768.png`, `modern-home-352.png`: rendered review captures.

Browser verification passed: selected-title artwork/details, simulated resume versus zero start, return focus, first-row and lower-row Up navigation, and search. No browser errors were recorded. Layout checks at content widths 320, 736 and 1024 pixels found no horizontal or vertical overflow; all artwork loaded. Default desktop layout is 16:9. Long-title text fallback and all three density choices were checked. This establishes mockup behavior only; no native build, physical D-pad test, accessibility certification or Shield performance test occurred.

## Proposed implementation mapping

Use HomeFragment for composition, ElegantFinHomePreview for artwork and title presentation, the existing HomeRowsFragment selection bridge for focus-driven updates, and CardPresenter for progress presentation. Reuse the existing image-loading infrastructure with bounded requests and cancellation when selection changes. Keep the accepted resume handling intact.

Next: review the mockup, then implement the agreed native home layout as a separate candidate. Details/browsing/player refinements remain later work. Preserve the installed dev6 and its rollback resources throughout candidate development.
