package org.jellyfin.androidtv.ui.browsing

import android.content.res.ColorStateList
import android.graphics.Color as AndroidColor
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import org.jellyfin.androidtv.databinding.HorizontalGridBrowseBinding
import org.jellyfin.androidtv.databinding.EnhancedDetailBrowseBinding
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.home.libraryArtwork
import org.jellyfin.androidtv.util.ImageHelper
import org.jellyfin.androidtv.util.TimeUtils
import org.jellyfin.androidtv.util.apiclient.*
import org.jellyfin.androidtv.util.dp
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.CollectionType
import org.jellyfin.sdk.model.api.ImageType
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun supportsModernLibrary(type: CollectionType?) = type == CollectionType.MOVIES || type == CollectionType.TVSHOWS

/** A display-only header; Leanback continues to own selection, clicks, paging and focus. */
class ModernLibraryBrowser private constructor(root: RelativeLayout, private val folder: BaseItemDto) {
	private val selected = MutableStateFlow<BaseItemDto?>(null)
	val header = ComposeView(root.context).apply {
		id = View.generateViewId()
		isFocusable = false
		setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
		setContent {
			val item by selected.collectAsState()
			JellyfinTheme { SelectedItemPreview(folder.name.orEmpty(), item, folder.libraryArtwork()) }
		}
	}
	init {
		root.setBackgroundColor(AndroidColor.rgb(15, 23, 42))
		root.addView(header, 0, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 184.dp(root.context)))
	}
	fun select(item: BaseItemDto?) { selected.value = item }

	companion object : KoinComponent {
		private val preferences by inject<UserPreferences>()
		@JvmStatic fun isEnabled(folder: BaseItemDto?) = preferences[UserPreferences.appTheme] == AppTheme.ELEGANT_FIN &&
			(supportsModernLibrary(folder?.collectionType) || folder?.type == BaseItemKind.BOX_SET)

		@JvmStatic fun install(binding: HorizontalGridBrowseBinding, folder: BaseItemDto): ModernLibraryBrowser? {
			if (!isEnabled(folder)) return null
			val browser = ModernLibraryBrowser(binding.root, folder)
			binding.title.visibility = View.INVISIBLE
			binding.infoRow.visibility = View.GONE
			binding.toolBar.apply {
				alpha = 1f
				layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48.dp(context)).apply {
					addRule(RelativeLayout.BELOW, browser.header.id)
					marginStart = 48.dp(context); marginEnd = 48.dp(context)
				}
			}
			(binding.rowsFragment.layoutParams as RelativeLayout.LayoutParams).apply {
				addRule(RelativeLayout.BELOW, binding.toolBar.id)
				topMargin = 8.dp(binding.root.context)
				binding.rowsFragment.layoutParams = this
			}
			binding.statusText.apply { alpha = 1f; setTextColor(AndroidColor.LTGRAY); maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END }
			(binding.statusText.layoutParams as RelativeLayout.LayoutParams).apply {
				addRule(RelativeLayout.START_OF, binding.counter.id)
				marginEnd = 16.dp(binding.root.context)
				binding.statusText.layoutParams = this
			}
			return browser
		}

		@JvmStatic fun install(binding: EnhancedDetailBrowseBinding, folder: BaseItemDto?): ModernLibraryBrowser? {
			if (folder == null || !isEnabled(folder)) return null
			val browser = ModernLibraryBrowser(binding.root, folder)
			binding.title.visibility = View.INVISIBLE
			binding.infoRow.visibility = View.GONE
			binding.summary.visibility = View.GONE
			(binding.rowsFragment.layoutParams as RelativeLayout.LayoutParams).apply {
				addRule(RelativeLayout.BELOW, browser.header.id)
				topMargin = 8.dp(binding.root.context)
				binding.rowsFragment.layoutParams = this
			}
			return browser
		}

		@JvmStatic fun styleTools(toolbar: LinearLayout) {
			val buttons = (0 until toolbar.childCount).map { toolbar.getChildAt(it) }.filterIsInstance<ImageButton>()
			val caption = TextView(toolbar.context).apply {
				text = buttons.firstOrNull()?.contentDescription
				setTextColor(AndroidColor.WHITE); textSize = 15f
				maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END
				gravity = android.view.Gravity.CENTER_VERTICAL
			}
			toolbar.addView(caption, 0, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
			for (button in buttons) {
				fun surface(fill: Int, stroke: Int) = GradientDrawable().apply {
					setColor(fill); cornerRadius = 10.dp(toolbar.context).toFloat()
					setStroke(2.dp(toolbar.context), stroke)
				}
				button.background = StateListDrawable().apply {
					addState(intArrayOf(android.R.attr.state_focused), surface(AndroidColor.WHITE, AndroidColor.rgb(139, 213, 255)))
					addState(intArrayOf(android.R.attr.state_activated), surface(AndroidColor.rgb(78, 52, 130), AndroidColor.rgb(167, 139, 250)))
					addState(intArrayOf(), surface(AndroidColor.rgb(30, 41, 59), AndroidColor.rgb(67, 82, 108)))
				}
				button.imageTintList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()), intArrayOf(AndroidColor.rgb(15, 23, 42), AndroidColor.WHITE))
				button.layoutParams = LinearLayout.LayoutParams(44.dp(toolbar.context), 44.dp(toolbar.context)).apply { marginStart = 10.dp(toolbar.context) }
				button.setPadding(10.dp(toolbar.context), 10.dp(toolbar.context), 10.dp(toolbar.context), 10.dp(toolbar.context))
				val previous = button.onFocusChangeListener
				button.setOnFocusChangeListener { view, focused ->
					previous?.onFocusChange(view, focused)
					if (focused) caption.text = button.contentDescription
				}
			}
		}
	}
}

@Composable
fun SelectedItemPreview(heading: String, item: BaseItemDto?, placeholder: Any? = null, compact: Boolean = false) {
	val api = koinInject<ApiClient>()
	val helper = koinInject<ImageHelper>()
	val navy = Color(0xFF0F172A)
	Box(Modifier.fillMaxSize().background(navy)) {
		key(item?.id, heading) {
			val artwork = remember(item, placeholder) {
				if (item == null) placeholder
				else (item.itemBackdropImages.firstOrNull() ?: item.parentBackdropImages.firstOrNull()
					?: item.itemImages[ImageType.THUMB])?.getUrl(api, maxWidth = 1280, maxHeight = 540)
			}
			Image(rememberAsyncImagePainter(artwork), null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
			Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(navy, navy.copy(alpha = 0.9f), navy.copy(alpha = 0.15f)))))
			Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent, navy))))
			Column(Modifier.padding(start = 48.dp, top = 12.dp, end = 48.dp).fillMaxWidth(0.7f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
				if (!compact) {
					if (item != null) Text(heading, color = Color(0xFFC4B5FD), fontSize = 12.sp, maxLines = 1)
					else Spacer(Modifier.height(14.dp))
				}
				val logoUrl = helper.getLogoImageUrl(item, 700)
				val logo = rememberAsyncImagePainter(logoUrl)
				val state by logo.state.collectAsState()
				Box(Modifier.fillMaxWidth().height(if (compact) 44.dp else 54.dp)) {
					if (state is AsyncImagePainter.State.Success) Image(logo, null, contentScale = ContentScale.Fit, alignment = androidx.compose.ui.Alignment.CenterStart, modifier = Modifier.fillMaxSize())
					else if (logoUrl == null || state is AsyncImagePainter.State.Error)
						Text(item?.name ?: heading, color = Color.White, fontSize = 25.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
				}
				Text(listOfNotNull(item?.productionYear?.toString(), item?.officialRating,
					item?.runTimeTicks?.takeIf { it > 0 }?.let { TimeUtils.formatMillis(it / 10_000) },
					item?.genres?.take(2)?.joinToString(" / ")).joinToString("  ·  "), color = Color.LightGray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
				val overview = remember(item?.overview) { HtmlCompat.fromHtml(item?.overview.orEmpty(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim() }
				Text(overview, color = Color.White, fontSize = 13.sp, lineHeight = 18.sp, maxLines = if (compact) 1 else 2, overflow = TextOverflow.Ellipsis)
			}
		}
	}
}
