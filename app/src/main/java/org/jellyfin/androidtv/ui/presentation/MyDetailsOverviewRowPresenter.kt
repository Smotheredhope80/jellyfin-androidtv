package org.jellyfin.androidtv.ui.presentation

import android.view.ViewGroup
import android.view.View
import androidx.core.view.isVisible
import androidx.leanback.widget.RowPresenter
import org.jellyfin.androidtv.ui.DetailRowView
import org.jellyfin.androidtv.ui.itemdetail.MyDetailsOverviewRow
import org.jellyfin.androidtv.ui.itemdetail.ModernDetailsStyle
import org.jellyfin.androidtv.util.InfoLayoutHelper
import org.jellyfin.androidtv.util.MarkdownRenderer
import org.jellyfin.androidtv.util.ImageHelper
import org.koin.java.KoinJavaComponent
import org.jellyfin.sdk.model.api.BaseItemKind

class MyDetailsOverviewRowPresenter(
	private val markdownRenderer: MarkdownRenderer,
) : RowPresenter() {
	class ViewHolder(
		private val detailRowView: DetailRowView,
		private val markdownRenderer: MarkdownRenderer,
	) : RowPresenter.ViewHolder(detailRowView) {
		private val binding get() = detailRowView.binding

		fun setItem(row: MyDetailsOverviewRow) {
			val modern = ModernDetailsStyle.isEnabled(row.item.type)
			detailRowView.setModernLayout(modern)
			setTitle(row.item.name)

			InfoLayoutHelper.addInfoRow(view.context, row.item, row.item.mediaSources?.getOrNull(row.selectedMediaSourceIndex), binding.fdMainInfoRow, false)
			binding.fdGenreRow.text = row.item.genres?.joinToString(" / ")

			binding.infoTitle1.text = row.infoItem1?.label
			binding.infoValue1.text = row.infoItem1?.value

			binding.infoTitle2.text = row.infoItem2?.label
			binding.infoValue2.text = row.infoItem2?.value

			binding.infoTitle3.text = row.infoItem3?.label
			binding.infoValue3.text = row.infoItem3?.value

			if (modern) {
				val childTitle = row.item.type == BaseItemKind.EPISODE || row.item.type == BaseItemKind.SEASON
				ModernDetailsStyle.applyTitleLayout(binding, childTitle)
				val logoUrl = KoinJavaComponent.get<ImageHelper>(ImageHelper::class.java).getLogoImageUrl(row.item, 1000)
				binding.fdTitle.isVisible = true
				// INVISIBLE retains measurement so the image loader can resolve the logo size.
				binding.mainImage.visibility = View.INVISIBLE
				binding.mainImage.contentDescription = row.item.name
				val imageBinding = binding
				binding.mainImage.loadWithResult(url = logoUrl) { loaded ->
					if (binding === imageBinding) {
						binding.fdTitle.isVisible = childTitle || !loaded
						binding.mainImage.visibility = if (loaded) View.VISIBLE else View.INVISIBLE
					}
				}
			} else {
				binding.fdTitle.isVisible = true
				binding.mainImage.isVisible = true
				binding.mainImage.load(row.imageDrawable, null, null, 1.0, 0)
			}

			setSummary(row.summary)

			if (row.item.type == BaseItemKind.PERSON) {
				binding.fdSummaryText.maxLines = 9
				binding.fdGenreRow.isVisible = false
			}

			val actions = row.actions
			val sameActions = binding.fdButtonRow.childCount == actions.size && actions.indices.all {
				binding.fdButtonRow.getChildAt(it) === actions[it]
			}
			// A metadata/artwork refresh must not detach the button currently holding focus.
			if (sameActions) return
			val focusedIndex = if (binding.fdButtonRow.hasFocus()) {
				binding.fdButtonRow.indexOfChild(binding.fdButtonRow.focusedChild)
			} else -1
			binding.fdButtonRow.removeAllViews()
			for (button in actions) {
				val parent = button.parent
				if (parent is ViewGroup) parent.removeView(button)

				if (modern) ModernDetailsStyle.applyButton(button)
				binding.fdButtonRow.addView(button)
			}
			if (focusedIndex >= 0) {
				(actions.getOrNull(focusedIndex)?.takeIf { it.isVisible } ?: actions.firstOrNull { it.isVisible })?.requestFocus()
			}
		}

		fun setTitle(title: String?) {
			binding.fdTitle.text = title
		}

		fun setSummary(summary: String?) {
			binding.fdSummaryText.text = summary?.let { markdownRenderer.toMarkdownSpanned(it) }
		}

		fun setInfoValue3(text: String?) {
			binding.infoValue3.text = text
		}
	}

	var viewHolder: ViewHolder? = null
		private set

	init {
		syncActivatePolicy = SYNC_ACTIVATED_CUSTOM
	}

	override fun createRowViewHolder(parent: ViewGroup): ViewHolder {
		val view = DetailRowView(parent.context)
		viewHolder = ViewHolder(view, markdownRenderer)
		return viewHolder!!
	}

	override fun onBindRowViewHolder(viewHolder: RowPresenter.ViewHolder, item: Any) {
		super.onBindRowViewHolder(viewHolder, item)
		if (item !is MyDetailsOverviewRow) return
		if (viewHolder !is ViewHolder) return

		viewHolder.setItem(item)
	}

	override fun onSelectLevelChanged(holder: RowPresenter.ViewHolder) = Unit
}
