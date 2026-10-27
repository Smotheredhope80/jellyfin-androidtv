package org.jellyfin.androidtv.ui.seerr

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import coil3.ImageLoader
import coil3.compose.AsyncImage
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.seerr.SeerrMediaType
import org.jellyfin.androidtv.data.seerr.SeerrRequestOutcome
import org.jellyfin.androidtv.data.seerr.SeerrTitle
import org.jellyfin.androidtv.data.seerr.tmdbImageUrl
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.base.button.Button
import org.jellyfin.androidtv.ui.base.form.Checkbox
import org.jellyfin.androidtv.ui.base.list.ListButton
import org.jellyfin.androidtv.ui.navigation.Destinations
import org.jellyfin.androidtv.ui.navigation.NavigationRepository
import org.jellyfin.androidtv.util.sdk.TrailerUtils
import timber.log.Timber
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class SeerrDetailsFragment : Fragment() {
	companion object {
		const val EXTRA_TYPE = "type"
		const val EXTRA_TMDB_ID = "tmdbId"
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	) = content {
		val type = SeerrMediaType.valueOf(requireArguments().getString(EXTRA_TYPE)!!)
		val tmdbId = requireArguments().getInt(EXTRA_TMDB_ID)

		JellyfinTheme {
			SeerrDetailsScreen(koinViewModel<SeerrDetailsViewModel> { parametersOf(type, tmdbId) })
		}
	}
}

@Composable
private fun SeerrDetailsScreen(viewModel: SeerrDetailsViewModel) {
	val navigationRepository = koinInject<NavigationRepository>()
	val context = LocalContext.current
	val resources = LocalResources.current
	val state by viewModel.state.collectAsState()
	val event by viewModel.events.collectAsState()
	val primaryFocus = remember { FocusRequester() }

	LaunchedEffect(event) {
		val message = when (val current = event) {
			null -> return@LaunchedEffect
			is SeerrRequestEvent.Done -> resources.getString(
				when (current.outcome) {
					SeerrRequestOutcome.APPROVED -> R.string.seerr_request_approved
					SeerrRequestOutcome.WAITING_FOR_APPROVAL -> R.string.seerr_request_waiting
					SeerrRequestOutcome.DECLINED -> R.string.seerr_request_declined
				}
			)
			is SeerrRequestEvent.Failed -> resources.getString(R.string.seerr_request_failed, current.message)
			SeerrRequestEvent.NotAllowed -> resources.getString(R.string.seerr_request_not_allowed)
		}
		Toast.makeText(context, message, Toast.LENGTH_LONG).show()
		viewModel.consumeEvent()
	}

	val title = state.title
	val options = state.options
	val optionsOpen = options != null
	val requestFocus = remember { FocusRequester() }
	LaunchedEffect(title != null) {
		if (title != null) primaryFocus.requestFocus()
	}

	// Return to the Request button when the options panel closes without a request
	var optionsWereOpen by remember { mutableStateOf(false) }
	LaunchedEffect(optionsOpen) {
		if (optionsWereOpen && !optionsOpen) {
			runCatching { requestFocus.requestFocus() }.onFailure { runCatching { primaryFocus.requestFocus() } }
		}
		optionsWereOpen = optionsOpen
	}

	BackHandler(enabled = optionsOpen) {
		if (options?.picker != null) viewModel.closePicker() else viewModel.closeOptions()
	}

	Box(Modifier.fillMaxSize().background(SeerrBackground)) {
		val imageLoader = koinInject<ImageLoader>()
		tmdbImageUrl(title?.backdropPath, "w1280")?.let { backdrop ->
			AsyncImage(
				model = backdrop,
				imageLoader = imageLoader,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize(),
			)
			Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(SeerrBackground, SeerrBackground.copy(alpha = 0.92f), SeerrBackground.copy(alpha = 0.35f)))))
			Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, SeerrBackground))))
		}

		when {
			state.signedOut -> Column(
				modifier = Modifier.padding(48.dp),
				verticalArrangement = Arrangement.spacedBy(14.dp),
			) {
				Text(stringResource(R.string.seerr_session_expired), color = Color.White, fontSize = 18.sp)
				Button(
					onClick = { navigationRepository.navigate(Destinations.seerrSetup, replace = true) },
					modifier = Modifier.focusRequester(primaryFocus),
				) { Text(stringResource(R.string.seerr_sign_in)) }
			}

			title == null -> Text(
				if (state.loading) stringResource(R.string.loading) else stringResource(R.string.seerr_load_failed, state.error.orEmpty()),
				color = Color.LightGray,
				modifier = Modifier.padding(48.dp),
			)

			options != null -> SeerrRequestOptionsPanel(
				title = title,
				options = options,
				requesting = state.requesting,
				seasonCount = state.selectedSeasons.size,
				viewModel = viewModel,
			)

			else -> SeerrTitleContent(
				title = title,
				state = state,
				primaryFocus = primaryFocus,
				requestFocus = requestFocus,
				onOpenInJellyfin = { itemId -> navigationRepository.navigate(Destinations.itemDetails(itemId)) },
				onRequest = viewModel::requestPressed,
				onTrailer = { key ->
					try {
						context.startActivity(TrailerUtils.getYoutubeTrailerIntent(context, key))
					} catch (err: ActivityNotFoundException) {
						Timber.w(err, "No activity for Seerr trailer")
						Toast.makeText(context, resources.getString(R.string.seerr_trailer_failed), Toast.LENGTH_LONG).show()
					}
				},
				onToggleSeason = viewModel::toggleSeason,
			)
		}
	}
}

@Composable
private fun SeerrTitleContent(
	title: SeerrTitle,
	state: SeerrDetailsState,
	primaryFocus: FocusRequester,
	requestFocus: FocusRequester,
	onOpenInJellyfin: (java.util.UUID) -> Unit,
	onRequest: () -> Unit,
	onTrailer: (String) -> Unit,
	onToggleSeason: (Int) -> Unit,
) {
	val imageLoader = koinInject<ImageLoader>()

	LazyColumn(
		contentPadding = PaddingValues(horizontal = 48.dp, vertical = 36.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier.fillMaxSize(),
	) {
		item(key = "header") {
			Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
				Box(
					Modifier
						.width(180.dp)
						.aspectRatio(2f / 3f)
						.clip(RoundedCornerShape(10.dp))
						.background(SeerrSurface)
				) {
					tmdbImageUrl(title.posterPath, "w342")?.let { poster ->
						AsyncImage(
							model = poster,
							imageLoader = imageLoader,
							contentDescription = null,
							contentScale = ContentScale.Crop,
							modifier = Modifier.fillMaxSize(),
						)
					}
				}

				Column(
					modifier = Modifier.fillMaxWidth(0.75f),
					verticalArrangement = Arrangement.spacedBy(10.dp),
				) {
					Text(title.title, color = Color.White, fontSize = 30.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
					title.info.tagline?.let {
						Text(it, color = Color.LightGray, fontSize = 14.sp, fontStyle = FontStyle.Italic, maxLines = 1, overflow = TextOverflow.Ellipsis)
					}
					Text(
						listOfNotNull(
							title.year,
							title.runtimeMinutes?.let { stringResource(R.string.seerr_runtime_minutes, it) },
							seerrAvailabilityLabel(title.availability),
						).joinToString("  ·  "),
						color = SeerrAccent,
						fontSize = 14.sp,
					)
					seerrRatingsLine(title, state.ratings)?.let {
						Text(it, color = Color.White, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
					}
					title.overview?.takeIf { it.isNotBlank() }?.let {
						// Shorter than phase 1 so the info lines below the buttons stay on screen
						Text(it, color = Color.White, fontSize = 14.sp, lineHeight = 20.sp, maxLines = 4, overflow = TextOverflow.Ellipsis)
					}

					Row(
						horizontalArrangement = Arrangement.spacedBy(12.dp),
						modifier = Modifier.padding(top = 8.dp).focusRestorer().focusGroup(),
					) {
						var focusAssigned = false
						fun primary(): Modifier = if (focusAssigned) Modifier else Modifier.focusRequester(primaryFocus).also { focusAssigned = true }

						title.jellyfinItemId?.let { itemId ->
							Button(onClick = { onOpenInJellyfin(itemId) }, modifier = primary()) {
								Text(stringResource(R.string.seerr_open_in_jellyfin))
							}
						}

						if (title.canRequest) {
							val count = state.selectedSeasons.size
							Button(
								onClick = onRequest,
								enabled = !state.requesting && (title.type == SeerrMediaType.MOVIE || count > 0),
								modifier = primary().focusRequester(requestFocus),
							) {
								Text(
									when {
										state.requesting -> stringResource(R.string.seerr_requesting)
										title.type == SeerrMediaType.TV -> pluralStringResource(R.plurals.seerr_request_seasons, count, count)
										else -> stringResource(R.string.seerr_request)
									}
								)
							}
						}

						// Never autoplays: the trailer only opens from this button
						title.trailerKey?.let { key ->
							Button(onClick = { onTrailer(key) }, modifier = primary()) {
								Text(stringResource(R.string.seerr_trailer))
							}
						}

						// Keep something focusable so Back and D-pad always have a target
						if (!focusAssigned) {
							Button(onClick = {}, enabled = true, modifier = primary()) {
								Text(seerrAvailabilityLabel(title.availability) ?: stringResource(R.string.seerr_status_pending))
							}
						}
					}

					// Not focusable, and one line each, so nothing sits out of reach below the buttons
					seerrInfoLines(title).forEach { line ->
						Text(line, color = Color.LightGray, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
					}
				}
			}
		}

		if (title.type == SeerrMediaType.TV && title.seasons.isNotEmpty()) {
			item(key = "seasons-heading") {
				Text(stringResource(R.string.seerr_seasons), color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
			}

			items(title.seasons, key = { "season-${it.number}" }) { season ->
				ListButton(
					onClick = { onToggleSeason(season.number) },
					enabled = season.requestable && !state.requesting,
					headingContent = { Text(season.name) },
					captionContent = {
						Text(
							listOfNotNull(
								pluralStringResource(R.plurals.seerr_season_episodes, season.episodeCount, season.episodeCount),
								seerrAvailabilityLabel(season.availability),
							).joinToString("  ·  ")
						)
					},
					trailingContent = {
						if (season.requestable) Checkbox(checked = season.number in state.selectedSeasons)
					},
					modifier = Modifier.fillMaxWidth(0.6f),
				)
			}
		}
	}
}
