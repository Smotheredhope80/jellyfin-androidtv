package org.jellyfin.androidtv.ui.player.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.base.LocalTextStyle
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.player.base.PlayerHeader
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.androidtv.util.apiclient.itemImages
import org.jellyfin.androidtv.util.apiclient.parentImages
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ImageType
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
					modifier = Modifier.height(72.dp),
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
						fontSize = 22.sp
					)
				)
			}

			if (!item.seriesName.isNullOrEmpty()) {
				Text(
					text = item.name.orEmpty(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1,
					style = LocalTextStyle.current.copy(
						color = Color.White,
						fontSize = 18.sp
					)
				)
			}
		}
	}
}
