package org.jellyfin.androidtv.ui.seerr

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.seerr.SeerrAvailability
import org.jellyfin.androidtv.data.seerr.SeerrCard
import org.jellyfin.androidtv.data.seerr.SeerrMediaType
import org.jellyfin.androidtv.data.seerr.SeerrRatings
import org.jellyfin.androidtv.data.seerr.SeerrTitle
import org.jellyfin.androidtv.data.seerr.tmdbImageUrl
import org.jellyfin.androidtv.ui.base.Text
import org.koin.compose.koinInject
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.Locale

internal val SeerrBackground = Color(0xFF0F172A)
internal val SeerrSurface = Color(0xFF1E293B)
internal val SeerrAccent = Color(0xFFC4B5FD)
internal val SeerrFocus = Color(0xFFA78BFA)

/** Number of cards from the end of a row at which the next page is loaded. */
internal const val SEERR_PAGE_AHEAD = 5

@Composable
fun seerrAvailabilityLabel(availability: SeerrAvailability): String? = when (availability) {
	SeerrAvailability.NONE -> null
	SeerrAvailability.PENDING -> stringResource(R.string.seerr_status_pending)
	SeerrAvailability.PROCESSING -> stringResource(R.string.seerr_status_processing)
	SeerrAvailability.PARTIAL -> stringResource(R.string.seerr_status_partial)
	SeerrAvailability.AVAILABLE -> stringResource(R.string.seerr_status_available)
}

/** `Rotten Tomatoes 85% · Audience 65% · IMDb 6.5 · TMDB 7.2`, text only; null when nothing is known. */
@Composable
fun seerrRatingsLine(title: SeerrTitle, ratings: SeerrRatings?): String? = listOfNotNull(
	ratings?.rtCritics?.let { stringResource(R.string.seerr_rating_rt, it) },
	ratings?.rtAudience?.let { stringResource(R.string.seerr_rating_rt_audience, it) },
	ratings?.imdb?.let { stringResource(R.string.seerr_rating_imdb, oneDecimal(it)) },
	title.tmdbScore?.let { stringResource(R.string.seerr_rating_tmdb, oneDecimal(it)) },
).takeIf { it.isNotEmpty() }?.joinToString(SEERR_SEPARATOR)

/** Up to five single lines of extra details for a film or show, skipping anything unknown. */
@Composable
fun seerrInfoLines(title: SeerrTitle): List<String> {
	val info = title.info
	fun line(vararg parts: String?) = parts.filterNotNull().filter { it.isNotBlank() }.takeIf { it.isNotEmpty() }?.joinToString(SEERR_SEPARATOR)

	val ratingAndGenres = line(info.certification, info.genres.takeIf { it.isNotEmpty() }?.joinToString(", "))
	val cast = info.cast.takeIf { it.isNotEmpty() }?.let { stringResource(R.string.seerr_cast, it.joinToString(", ")) }
	val credit = info.credit.takeIf { it.isNotEmpty() }?.joinToString(", ")
	val companies = info.companies.takeIf { it.isNotEmpty() }?.joinToString(", ")

	return when (title.type) {
		SeerrMediaType.MOVIE -> listOfNotNull(
			ratingAndGenres,
			line(formatSeerrDate(info.date), info.status),
			line(
				info.revenue?.let { stringResource(R.string.seerr_box_office, it) },
				info.budget?.let { stringResource(R.string.seerr_budget, it) },
			),
			line(credit?.let { stringResource(R.string.seerr_directed_by, it) }, companies),
			cast,
		)

		SeerrMediaType.TV -> {
			val first = formatSeerrDate(info.date)
			val last = formatSeerrDate(info.lastAirDate)
			val aired = if (first != null && last != null && first != last) stringResource(R.string.seerr_date_range, first, last) else first
			listOfNotNull(
				ratingAndGenres,
				line(aired, info.episodeRuntimeMinutes?.let { stringResource(R.string.seerr_episode_runtime, it) }, info.status),
				line(companies, formatSeerrDate(info.nextEpisodeDate)?.let { stringResource(R.string.seerr_next_episode, it) }),
				credit?.let { stringResource(R.string.seerr_created_by, it) },
				cast,
			)
		}
	}
}

internal const val SEERR_SEPARATOR = "  ·  "

private fun oneDecimal(value: Double) = String.format(Locale.getDefault(), "%.1f", value)

/** Formats Seerr's `yyyy-MM-dd` dates for the current locale; other text is returned unchanged. */
fun formatSeerrDate(value: String?): String? {
	if (value.isNullOrBlank()) return null
	return try {
		LocalDate.parse(value.take(10)).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
	} catch (err: DateTimeParseException) {
		Timber.d(err, "Unexpected Seerr date")
		value
	}
}

@Composable
fun SeerrTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	password: Boolean = false,
	keyboardType: KeyboardType = KeyboardType.Text,
	imeAction: ImeAction = ImeAction.Next,
	onImeAction: () -> Unit = {},
) {
	val interactionSource = remember { MutableInteractionSource() }
	val focused by interactionSource.collectIsFocusedAsState()
	val textColor = if (focused) SeerrBackground else Color.White

	Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
		Text(label, color = SeerrAccent, fontSize = 12.sp)
		BasicTextField(
			value = value,
			onValueChange = onValueChange,
			singleLine = true,
			interactionSource = interactionSource,
			textStyle = TextStyle(color = textColor, fontSize = 16.sp),
			cursorBrush = SolidColor(textColor),
			visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
			keyboardOptions = KeyboardOptions.Default.copy(
				keyboardType = if (password) KeyboardType.Password else keyboardType,
				imeAction = imeAction,
				autoCorrectEnabled = false,
				showKeyboardOnFocus = true,
			),
			keyboardActions = KeyboardActions { onImeAction() },
			decorationBox = { inner ->
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.background(if (focused) Color.White else SeerrSurface, RoundedCornerShape(10.dp))
						.border(2.dp, if (focused) SeerrFocus else Color(0xFF43526C), RoundedCornerShape(10.dp))
						.padding(horizontal = 14.dp, vertical = 10.dp)
				) {
					if (value.isEmpty() && placeholder != null) Text(placeholder, color = textColor.copy(alpha = 0.6f), fontSize = 16.sp)
					inner()
				}
			},
		)
	}
}

@Composable
fun SeerrPosterCard(
	card: SeerrCard,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val focused by interactionSource.collectIsFocusedAsState()
	val imageLoader = koinInject<ImageLoader>()
	val shape = RoundedCornerShape(8.dp)

	Column(
		modifier = modifier
			.width(124.dp)
			.clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(2f / 3f)
				.graphicsLayer {
					val scale = if (focused) 1.06f else 1f
					scaleX = scale
					scaleY = scale
				}
				.clip(shape)
				.background(SeerrSurface, shape)
				.border(if (focused) 3.dp else 0.dp, if (focused) Color.White else Color.Transparent, shape)
		) {
			val posterUrl = tmdbImageUrl(card.posterPath, "w342")
			if (posterUrl != null) {
				AsyncImage(
					model = posterUrl,
					imageLoader = imageLoader,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.fillMaxSize(),
				)
			} else {
				Text(
					card.title,
					color = Color.White,
					fontSize = 13.sp,
					textAlign = TextAlign.Center,
					modifier = Modifier.align(Alignment.Center).padding(8.dp),
				)
			}

			seerrAvailabilityLabel(card.availability)?.let { label ->
				Text(
					label,
					color = SeerrBackground,
					fontSize = 10.sp,
					maxLines = 1,
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(5.dp)
						.background(if (card.availability == SeerrAvailability.AVAILABLE) Color(0xFF86EFAC) else SeerrAccent, RoundedCornerShape(6.dp))
						.padding(horizontal = 6.dp, vertical = 2.dp),
				)
			}
		}

		Text(
			listOfNotNull(card.title, card.year).joinToString(" · "),
			color = if (focused) Color.White else Color.LightGray,
			fontSize = 12.sp,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
		)
	}
}

@Composable
fun SeerrCardRow(
	title: String,
	cards: List<SeerrCard>,
	onCardClick: (SeerrCard) -> Unit,
	modifier: Modifier = Modifier,
	message: String? = null,
	onNearEnd: () -> Unit = {},
	firstCardFocusRequester: FocusRequester? = null,
) {
	Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
		Text(title, color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 48.dp))

		if (cards.isEmpty()) {
			Text(message.orEmpty(), color = Color.LightGray, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 48.dp, vertical = 8.dp))
		} else {
			LazyRow(
				contentPadding = PaddingValues(horizontal = 48.dp, vertical = 10.dp),
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.focusRestorer()
					.focusGroup(),
			) {
				itemsIndexed(cards, key = { _, card -> "${card.type}-${card.tmdbId}" }) { index, card ->
					SeerrPosterCard(
						card = card,
						onClick = { onCardClick(card) },
						modifier = Modifier
							.then(if (index == 0 && firstCardFocusRequester != null) Modifier.focusRequester(firstCardFocusRequester) else Modifier)
							.onFocusChanged { if (it.isFocused && index >= cards.size - SEERR_PAGE_AHEAD) onNearEnd() },
					)
				}
			}
		}
	}
}
