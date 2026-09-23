package org.jellyfin.androidtv.ui.presentation

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.findViewTreeCompositionContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.leanback.widget.Presenter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.flow.MutableStateFlow
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.GridButton
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.itemhandling.GridButtonBaseRowItem
import org.koin.compose.koinInject

class GridButtonPresenter @JvmOverloads constructor(
	private val width: Int = 110,
	private val imageHeight: Int = 110,
) : Presenter() {
	private class ComposeViewWrapper(composeView: ComposeView) : FrameLayout(composeView.context) {
		init {
			isFocusable = true
			isFocusableInTouchMode = true
			descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
			addView(composeView)
		}

		// Hack to prevent Compose crash with leanback presenters
		override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
			if (isAttachedToWindow) super.onMeasure(widthMeasureSpec, heightMeasureSpec)
			else setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
		}
	}

	inner class ViewHolder(
		private val composeView: ComposeView,
	) : Presenter.ViewHolder(ComposeViewWrapper(composeView)) {
		private val _item = MutableStateFlow<GridButton?>(null)
		private val _focused = MutableStateFlow(false)

		init {
			composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
			_focused.value = view.isFocused
			view.setOnFocusChangeListener { _, focused ->
				_focused.value = focused
			}

			composeView.setContent {
				val button by _item.collectAsState()
				val focused by _focused.collectAsState()

				if (button != null) {
					GridButtonView(
						value = button!!,
						focused = focused,
						width = width,
						imageHeight = imageHeight,
					)
				}
			}
		}

		fun bind(value: GridButton) {
			_item.value = value
			_focused.value = view.isFocused
		}

		fun unbind() {
			_item.value = null
			_focused.value = false
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
		val composeView = ComposeView(parent.context).apply {
			setParentCompositionContext(parent.findViewTreeCompositionContext())
			setViewTreeLifecycleOwner(parent.findViewTreeLifecycleOwner())
			setViewTreeSavedStateRegistryOwner(parent.findViewTreeSavedStateRegistryOwner())
		}
		return ViewHolder(composeView)
	}

	override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any?) {
		if (viewHolder !is ViewHolder) return

		when (item) {
			is GridButtonBaseRowItem -> viewHolder.bind(item.gridButton)
			is GridButton -> viewHolder.bind(item)
		}
	}

	override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
		(viewHolder as? ViewHolder)?.unbind()
	}

	override fun onViewAttachedToWindow(viewHolder: Presenter.ViewHolder) = Unit
}

@Composable
private fun GridButtonView(
	value: GridButton,
	focused: Boolean,
	width: Int,
	imageHeight: Int,
) {
	val userPreferences = koinInject<UserPreferences>()
	val elegantFin = userPreferences[UserPreferences.appTheme] == AppTheme.ELEGANT_FIN

	if (elegantFin) {
		val shape = RoundedCornerShape(12.dp)
		val hasImage = value.imageRes != null

		if (hasImage) {
			val borderModifier = if (focused) {
				Modifier.border(2.dp, colorResource(R.color.theme_elegantfin_accent), shape)
			} else {
				Modifier.border(1.dp, Color(0x26FFFFFF), shape)
			}
			Box(
				modifier = Modifier
					.size(width.dp, imageHeight.dp)
					.then(borderModifier)
					.clip(shape)
					.background(
						if (focused) colorResource(R.color.theme_elegantfin_accent).copy(alpha = 0.35f)
						else colorResource(R.color.theme_elegantfin_surface).copy(alpha = 0.70f)
					)
			) {
				Image(
					painter = painterResource(value.imageRes),
					contentDescription = value.text,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size(width.dp, imageHeight.dp)
				)
				Text(
					text = value.text,
					style = TextStyle(
						color = Color.White,
						fontSize = 13.sp,
						fontWeight = FontWeight.SemiBold,
					),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier
						.padding(horizontal = 14.dp, vertical = 10.dp)
						.align(Alignment.BottomStart)
				)
			}
		} else {
			// Pill button for text-only views (Suggested, All items, By letter, Genres, Albums, Artists, Random, etc.)
			val buttonWidth = maxOf(width, 136).dp
			val borderModifier = if (focused) {
				Modifier.border(2.dp, colorResource(R.color.theme_elegantfin_accent), shape)
			} else {
				Modifier.border(1.dp, Color(0x26FFFFFF), shape)
			}
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.width(buttonWidth)
					.height(44.dp)
					.then(borderModifier)
					.clip(shape)
					.background(
						if (focused) colorResource(R.color.theme_elegantfin_accent).copy(alpha = 0.35f)
						else colorResource(R.color.theme_elegantfin_surface).copy(alpha = 0.70f)
					)
					.padding(horizontal = 12.dp)
			) {
				Text(
					text = value.text,
					style = TextStyle(
						color = Color.White,
						fontSize = 13.sp,
						fontWeight = FontWeight.SemiBold,
						textAlign = TextAlign.Center,
					),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
				)
			}
		}
	} else {
		// Legacy theme styling
		val shape = RoundedCornerShape(4.dp)
		val background = if (focused) {
			colorResource(R.color.button_default_highlight_background)
		} else {
			colorResource(R.color.button_default_normal_background)
		}
		val textColor = if (focused) {
			colorResource(R.color.button_default_highlight_text)
		} else {
			colorResource(R.color.button_default_normal_text)
		}

		Box(
			modifier = Modifier
				.width(width.dp)
				.clip(shape)
				.background(background)
		) {
			if (value.imageRes != null) {
				Image(
					painter = painterResource(value.imageRes),
					contentDescription = value.text,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size(width.dp, imageHeight.dp)
				)
			}

			Text(
				text = value.text,
				style = TextStyle(
					color = textColor,
					fontSize = 12.sp
				),
				modifier = Modifier
					.padding(15.dp, 10.dp)
					.align(Alignment.BottomStart)
			)
		}
	}
}
