package org.jellyfin.androidtv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.composable.getResolutionName
import org.jellyfin.androidtv.util.TimeUtils
import org.jellyfin.androidtv.util.ImageHelper
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.androidtv.util.apiclient.itemBackdropImages
import org.jellyfin.androidtv.util.apiclient.itemImages
import org.jellyfin.androidtv.util.apiclient.parentBackdropImages
import org.jellyfin.androidtv.util.apiclient.parentImages
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType
import org.jellyfin.sdk.model.api.MediaStreamType
import org.jellyfin.sdk.model.api.VideoRangeType
import org.koin.compose.koinInject

/** Display only: artwork never adds a focus stop, requests metadata or starts playback. */
@Composable
fun ElegantFinHomePreview(item: BaseItemDto?, modifier: Modifier = Modifier, topInset: Dp = 0.dp) {
	val background = colorResource(R.color.theme_elegantfin_background_dark)
	val secondaryText = colorResource(R.color.theme_elegantfin_text_dim)
	val api = koinInject<ApiClient>()
	val imageHelper = koinInject<ImageHelper>()
	val librarySelection = item == null || item.type == BaseItemKind.USER_VIEW || item.type == BaseItemKind.COLLECTION_FOLDER
	val previewTitle = item?.name?.takeIf(String::isNotBlank) ?: stringResource(R.string.lbl_my_media)
	val metadata = remember(item) {
		listOfNotNull(
			item?.productionYear?.toString(),
			item?.officialRating?.takeIf(String::isNotBlank),
			item?.runTimeTicks?.takeIf { it > 0 }?.let { TimeUtils.formatMillis(it / 10_000) },
			item?.genres?.take(2)?.joinToString(" · ")?.takeIf(String::isNotBlank),
		).joinToString("  ·  ")
	}
	val overview = remember(item?.overview) {
		HtmlCompat.fromHtml(item?.overview.orEmpty(), HtmlCompat.FROM_HTML_MODE_LEGACY)
			.toString().replace(Regex("\\s+"), " ").trim()
	}
	val progress = item?.homePlaybackProgress()
	Box(modifier.fillMaxWidth().height(168.dp + topInset).background(background)) {
		// Keying disposes the previous image painter immediately when focus changes.
		key(item?.id) {
			val backdrop = remember(item) {
				item?.itemBackdropImages?.firstOrNull() ?: item?.parentBackdropImages?.firstOrNull()
					?: item?.itemImages?.get(ImageType.THUMB)
					?: item?.itemImages?.get(ImageType.PRIMARY)?.takeIf { librarySelection }
			}
			val localArtwork = item?.libraryArtwork() ?: R.drawable.library_collections.takeIf { item == null }
			val backdropPainter = rememberAsyncImagePainter(localArtwork ?: backdrop?.getUrl(api, maxWidth = 1280, maxHeight = 720))
			Image(
				painter = backdropPainter,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				alignment = Alignment.TopCenter,
				modifier = Modifier.fillMaxSize(),
			)
		}
		if (!librarySelection) Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(
			0f to background, 0.36f to background.copy(alpha = 0.94f), 0.72f to background.copy(alpha = 0.25f),
			1f to background.copy(alpha = 0.1f),
		)))
		Box(Modifier.fillMaxSize().background(Brush.verticalGradient(
			0f to Color.Transparent, 0.75f to Color.Transparent, 1f to background,
		)))
		if (!librarySelection) Column(
			modifier = Modifier.padding(start = 48.dp, end = 48.dp, top = topInset + 4.dp, bottom = 4.dp).fillMaxWidth(0.58f),
			verticalArrangement = Arrangement.spacedBy(4.dp),
		) {
			key(item?.id) {
				val logo = remember(item) { imageHelper.getLogoImageUrl(item, 600) }
				val logoPainter = rememberAsyncImagePainter(logo)
				val logoState by logoPainter.state.collectAsState()
				Box(Modifier.fillMaxWidth().height(42.dp), contentAlignment = Alignment.CenterStart) {
					if (logoState is AsyncImagePainter.State.Success) {
						Image(logoPainter, contentDescription = item?.name, contentScale = ContentScale.Fit,
							alignment = Alignment.CenterStart, modifier = Modifier.fillMaxSize())
					} else {
						Text(previewTitle, color = Color.White, fontSize = 22.sp,
							fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
					}
				}
			}
			Text(metadata,
				color = secondaryText, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
			HomeFormatBadges(item)
			Text(overview, color = Color.White, fontSize = 13.sp, lineHeight = 17.sp,
				maxLines = 2, overflow = TextOverflow.Ellipsis)
			if (progress != null) {
				Text(
					text = stringResource(R.string.lbl_resume_from, TimeUtils.formatMillis(progress.positionMillis)),
					color = colorResource(R.color.theme_elegantfin_accent), fontSize = 11.sp, maxLines = 1,
				)
			}
		}
	}
}

@Composable
private fun HomeFormatBadges(item: BaseItemDto?) {
	val video = item?.mediaStreams?.firstOrNull { it.type == MediaStreamType.VIDEO }
	val audio = item?.mediaStreams?.firstOrNull { it.type == MediaStreamType.AUDIO }
	val resolution = video?.width?.let { getResolutionName(it, video.height ?: 0, video.isInterlaced) }
	val range = when (video?.videoRangeType) {
		VideoRangeType.HDR10 -> stringResource(R.string.hdr10)
		VideoRangeType.HDR10_PLUS -> stringResource(R.string.hdr10_plus)
		VideoRangeType.HLG -> stringResource(R.string.hlg)
		VideoRangeType.DOVI, VideoRangeType.DOVI_WITH_EL, VideoRangeType.DOVI_WITH_HDR10,
		VideoRangeType.DOVI_WITH_HLG, VideoRangeType.DOVI_WITH_SDR, VideoRangeType.DOVI_WITH_HDR10_PLUS,
		VideoRangeType.DOVI_WITH_ELHDR10_PLUS -> stringResource(R.string.dolby_vision)
		else -> null
	}
	val audioLabel = if (audio?.profile?.contains("Dolby Atmos", ignoreCase = true) == true) {
		stringResource(R.string.dolby_atmos)
	} else audio?.codec?.uppercase()
	val labels = listOfNotNull(resolution, range, audioLabel)
	if (labels.isEmpty()) return
	Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
		labels.forEach { label ->
			Text(label, color = Color.White, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
				modifier = Modifier.weight(1f, fill = false)
					.background(colorResource(R.color.theme_elegantfin_surface), RoundedCornerShape(3.dp))
					.padding(horizontal = 5.dp, vertical = 3.dp))
		}
	}
}
