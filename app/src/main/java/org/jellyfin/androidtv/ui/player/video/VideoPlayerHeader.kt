package org.jellyfin.androidtv.ui.player.video

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.getResolutionName
import org.jellyfin.androidtv.ui.base.LocalTextStyle
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.player.base.PlayerHeader
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.androidtv.util.apiclient.itemImages
import org.jellyfin.androidtv.util.apiclient.parentImages
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ImageType
import org.jellyfin.sdk.model.api.MediaStreamType
import org.jellyfin.sdk.model.api.VideoRangeType
import org.koin.compose.koinInject

@Composable
@Stable
fun VideoPlayerHeader(
	item: BaseItemDto?,
) {
	val api = koinInject<ApiClient>()
	val logo = item?.itemImages?.get(ImageType.LOGO) ?: item?.parentImages?.get(ImageType.LOGO)

	PlayerHeader {
		if (item != null) {
			if (logo != null) {
				AsyncImage(
					modifier = Modifier.height(64.dp),
					url = logo.getUrl(api, maxWidth = 440),
					blurHash = logo.blurHash,
					aspectRatio = logo.aspectRatio ?: 1f,
				)
			} else {
				Text(
					text = item.seriesName ?: item.name.orEmpty(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1,
					style = LocalTextStyle.current.copy(
						color = Color.White,
						fontSize = 22.sp,
						fontWeight = FontWeight.Bold
					)
				)
			}

			if (!item.seriesName.isNullOrEmpty()) {
				Text(
					text = item.name.orEmpty(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1,
					style = LocalTextStyle.current.copy(
						color = Color(0xFFCBD5E1),
						fontSize = 16.sp
					)
				)
			}

			PlayerFormatBadges(item)
		}
	}
}

private data class PlayerBadge(
	val text: String,
	val textColor: Color,
	val backgroundColor: Color,
	val borderColor: Color,
)

@Composable
private fun PlayerFormatBadges(item: BaseItemDto?) {
	if (item == null) return
	val video = item.mediaStreams?.firstOrNull { it.type == MediaStreamType.VIDEO }
	val audio = item.mediaStreams?.firstOrNull { it.type == MediaStreamType.AUDIO }

	val badges = buildList {
		// Resolution badge (4K UHD emerald, 1080p blue)
		val resolution = video?.width?.let { getResolutionName(it, video.height ?: 0, video.isInterlaced) }
		if (!resolution.isNullOrBlank()) {
			val is4k = resolution.contains("4K", ignoreCase = true)
			add(
				PlayerBadge(
					text = resolution,
					textColor = if (is4k) Color(0xFF34D399) else Color(0xFF93C5FD),
					backgroundColor = if (is4k) Color(0x33059669) else Color(0x2E2563EB),
					borderColor = if (is4k) Color(0x8010B981) else Color(0x803B82F6),
				)
			)
		}

		// Dynamic range badge (Dolby Vision purple, HDR amber)
		val isDovi = when (video?.videoRangeType) {
			VideoRangeType.DOVI, VideoRangeType.DOVI_WITH_EL, VideoRangeType.DOVI_WITH_HDR10,
			VideoRangeType.DOVI_WITH_HLG, VideoRangeType.DOVI_WITH_SDR, VideoRangeType.DOVI_WITH_HDR10_PLUS,
			VideoRangeType.DOVI_WITH_ELHDR10_PLUS -> true
			else -> false
		}
		val rangeText = when {
			isDovi -> stringResource(R.string.dolby_vision)
			video?.videoRangeType == VideoRangeType.HDR10_PLUS -> stringResource(R.string.hdr10_plus)
			video?.videoRangeType == VideoRangeType.HDR10 -> stringResource(R.string.hdr10)
			video?.videoRangeType == VideoRangeType.HLG -> stringResource(R.string.hlg)
			else -> null
		}
		if (!rangeText.isNullOrBlank()) {
			add(
				PlayerBadge(
					text = rangeText,
					textColor = if (isDovi) Color(0xFFD8B4FE) else Color(0xFFFDE047),
					backgroundColor = if (isDovi) Color(0x38A855F7) else Color(0x38EAB308),
					borderColor = if (isDovi) Color(0x80A855F7) else Color(0x80EAB308),
				)
			)
		}

		// Audio badge (Dolby Atmos / TrueHD sky blue, DTS cyan)
		val isAtmos = audio?.profile?.contains("Dolby Atmos", ignoreCase = true) == true ||
			audio?.title?.contains("atmos", ignoreCase = true) == true
		val audioText = if (isAtmos) {
			stringResource(R.string.dolby_atmos)
		} else {
			audio?.codec?.uppercase()
		}
		if (!audioText.isNullOrBlank()) {
			val isPremiumAudio = isAtmos || audioText.contains("TRUEHD", ignoreCase = true)
			add(
				PlayerBadge(
					text = audioText,
					textColor = if (isPremiumAudio) Color(0xFF7DD3FC) else Color(0xFF67E8F9),
					backgroundColor = if (isPremiumAudio) Color(0x380EA5E9) else Color(0x300891B2),
					borderColor = if (isPremiumAudio) Color(0x800EA5E9) else Color(0x8006B6D4),
				)
			)
		}

		// Year
		item.productionYear?.toString()?.takeIf { it.isNotBlank() }?.let { yr ->
			add(
				PlayerBadge(
					text = yr,
					textColor = Color(0xFFE2E8F0),
					backgroundColor = Color(0x26FFFFFF),
					borderColor = Color(0x40FFFFFF),
				)
			)
		}

		// Rating
		item.officialRating?.takeIf { it.isNotBlank() }?.let { rt ->
			add(
				PlayerBadge(
					text = rt,
					textColor = Color(0xFFE2E8F0),
					backgroundColor = Color(0x26FFFFFF),
					borderColor = Color(0x40FFFFFF),
				)
			)
		}

		// Runtime
		item.runTimeTicks?.let { ticks ->
			val totalMinutes = (ticks / 10000000L) / 60
			val h = totalMinutes / 60
			val m = totalMinutes % 60
			val formatted = if (h > 0) "${h}h ${m}m" else "${m}m"
			add(
				PlayerBadge(
					text = formatted,
					textColor = Color(0xFFCBD5E1),
					backgroundColor = Color(0x26FFFFFF),
					borderColor = Color(0x40FFFFFF),
				)
			)
		}
	}

	if (badges.isEmpty()) return

	Row(
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.padding(top = 4.dp)
	) {
		badges.forEach { badge ->
			Text(
				text = badge.text,
				color = badge.textColor,
				fontSize = 11.sp,
				fontWeight = FontWeight.Bold,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				modifier = Modifier
					.background(
						color = badge.backgroundColor,
						shape = RoundedCornerShape(4.dp)
					)
					.border(
						width = 1.dp,
						color = badge.borderColor,
						shape = RoundedCornerShape(4.dp)
					)
					.padding(horizontal = 7.dp, vertical = 2.dp)
			)
		}
	}
}
