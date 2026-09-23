package org.jellyfin.androidtv.ui.itemdetail

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ImageView
import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import org.jellyfin.androidtv.databinding.ViewRowDetailsBinding
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.TextUnderButton
import org.jellyfin.androidtv.util.dp
import org.jellyfin.sdk.model.api.BaseItemKind
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/** Presentation only: the existing detail buttons retain their click and focus listeners. */
object ModernDetailsStyle : KoinComponent {
	private val preferences by inject<UserPreferences>()

	@JvmStatic
	fun isEnabled(type: BaseItemKind?) = preferences[UserPreferences.appTheme] == AppTheme.ELEGANT_FIN &&
		type in setOf(BaseItemKind.MOVIE, BaseItemKind.SERIES, BaseItemKind.SEASON, BaseItemKind.EPISODE)

	fun applyLayout(binding: ViewRowDetailsBinding) {
		val context = binding.root.context
		val layout = binding.fdTitle.parent as ConstraintLayout
		val artwork = binding.mainImage.parent as View
		if (artwork.id == View.NO_ID) artwork.id = View.generateViewId()
		binding.root.layoutParams.height = 410.dp(context)
		val constraints = ConstraintSet().apply { clone(layout) }
		fun place(view: View, start: Int, top: Int, width: Int, height: Int, end: Int? = null) {
			constraints.clear(view.id)
			constraints.constrainWidth(view.id, if (end == null) width.dp(context) else 0)
			constraints.constrainHeight(view.id, height.dp(context))
			constraints.connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, start.dp(context))
			constraints.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top.dp(context))
			if (end != null) constraints.connect(view.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, end.dp(context))
		}
		place(artwork, 50, 66, 440, 82)
		place(binding.fdTitle, 50, 66, 0, 82, 50)
		place(binding.fdMainInfoRow, 50, 160, 0, 22, 50)
		place(binding.fdGenreRow, 50, 186, 0, 22, 50)
		place(binding.fdSummaryText, 50, 216, 0, 70, 50)
		place(binding.fdButtonRow, 50, 300, 0, 58, 50)
		binding.mainImage.apply {
			layoutParams = android.widget.FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
			)
			scaleType = ImageView.ScaleType.FIT_START
			background = null
		}
		// Existing run-time/end-time/season information remains available below the hero.
		val titles = listOf(binding.infoTitle1, binding.infoTitle2, binding.infoTitle3)
		val values = listOf(binding.infoValue1, binding.infoValue2, binding.infoValue3)
		for (index in titles.indices) {
			place(titles[index], 50 + index * 280, 370, 94, 24)
			place(values[index], 148 + index * 280, 370, 176, 24)
			for (text in listOf(titles[index], values[index])) {
				text.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
				text.textSize = 12f
				text.setTextColor(Color.WHITE)
				text.alpha = 0.8f
				text.maxLines = 1
				text.ellipsize = TextUtils.TruncateAt.END
			}
		}
		constraints.applyTo(layout)
		binding.fdTitle.apply {
			textSize = 32f
			typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
			gravity = Gravity.BOTTOM
			setTextColor(Color.WHITE)
		}
		binding.fdGenreRow.setTextColor(Color.LTGRAY)
		binding.fdSummaryText.apply {
			maxLines = 3
			gravity = Gravity.TOP or Gravity.START
			textAlignment = View.TEXT_ALIGNMENT_VIEW_START
			setTextColor(Color.WHITE)
		}
	}

	fun applyButton(button: TextUnderButton) {
		val context = button.context
		val colors = ColorStateList(
			arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
			intArrayOf(Color.rgb(15, 23, 42), Color.WHITE),
		)
		fun surface(color: Int, border: Int) = GradientDrawable().apply {
			setColor(color)
			cornerRadius = 12.dp(context).toFloat()
			setStroke(2.dp(context), border)
		}
		button.background = StateListDrawable().apply {
			addState(intArrayOf(android.R.attr.state_focused), surface(Color.WHITE, Color.rgb(139, 213, 255)))
			addState(intArrayOf(android.R.attr.state_activated), surface(Color.rgb(49, 64, 93), Color.rgb(139, 213, 255)))
			addState(intArrayOf(), surface(Color.rgb(25, 36, 57), Color.rgb(67, 82, 108)))
		}
		button.layoutParams = LinearLayout.LayoutParams(0, 54.dp(context), 1f).apply {
			marginEnd = 8.dp(context)
		}
		button.binding.root.layoutParams = android.widget.FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
		)
		button.binding.root.layoutDirection = View.LAYOUT_DIRECTION_LOCALE
		button.binding.imageButton.apply {
			background = null
			imageTintList = colors
			setPadding(0, 0, 0, 0)
			layoutParams = RelativeLayout.LayoutParams(22.dp(context), 22.dp(context)).apply {
				addRule(RelativeLayout.ALIGN_PARENT_START)
				addRule(RelativeLayout.CENTER_VERTICAL)
				marginStart = 10.dp(context)
			}
		}
		button.binding.label.apply {
			alpha = 1f
			isDuplicateParentStateEnabled = true
			setTextColor(colors)
			textSize = 12f
			maxLines = 3
			gravity = Gravity.CENTER_VERTICAL
			layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
				addRule(RelativeLayout.END_OF, button.binding.imageButton.id)
				addRule(RelativeLayout.ALIGN_PARENT_END)
			}
		}
	}

	fun applyTitleLayout(binding: ViewRowDetailsBinding, childTitle: Boolean) {
		val context = binding.root.context
		val layout = binding.fdTitle.parent as ConstraintLayout
		val artwork = binding.mainImage.parent as View
		ConstraintSet().apply {
			clone(layout)
			connect(artwork.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (if (childTitle) 38 else 66).dp(context))
			constrainHeight(artwork.id, (if (childTitle) 62 else 82).dp(context))
			connect(binding.fdTitle.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (if (childTitle) 106 else 66).dp(context))
			constrainHeight(binding.fdTitle.id, (if (childTitle) 44 else 82).dp(context))
			applyTo(layout)
		}
		binding.fdTitle.textSize = if (childTitle) 24f else 32f
	}
}
