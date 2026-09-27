package org.jellyfin.androidtv.ui.browsing

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import org.jellyfin.androidtv.ui.AsyncImageView
import org.jellyfin.androidtv.ui.itemdetail.ModernDetailsStyle
import org.jellyfin.androidtv.ui.itemdetail.ThemeMusicController
import org.jellyfin.androidtv.util.ImageHelper
import org.jellyfin.androidtv.util.dp
import org.koin.android.ext.android.inject

import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.querying.GetSpecialsRequest
import org.jellyfin.androidtv.data.repository.ItemRepository
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ItemFilter
import org.jellyfin.sdk.model.api.ItemSortBy
import org.jellyfin.sdk.model.api.SortOrder
import org.jellyfin.sdk.model.api.request.GetItemsRequest

class GenericFolderFragment : EnhancedBrowseFragment() {
	private var themeMusic: ThemeMusicController? = null
	private val imageHelper by inject<ImageHelper>()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (mFolder.type != BaseItemKind.SEASON) return
		themeMusic = ThemeMusicController(this).also { it.select(mFolder) }
		if (!ModernDetailsStyle.isEnabled(mFolder.type)) return
		val root = view as? RelativeLayout ?: return
		val logo = AsyncImageView(requireContext()).apply {
			id = View.generateViewId()
			visibility = View.INVISIBLE
			scaleType = ImageView.ScaleType.FIT_CENTER
			contentDescription = mFolder.seriesName
			isFocusable = false
		}
		root.addView(logo, RelativeLayout.LayoutParams(300.dp(requireContext()), 64.dp(requireContext())).apply {
			addRule(RelativeLayout.ALIGN_PARENT_END)
			topMargin = 12.dp(requireContext())
			marginEnd = 180.dp(requireContext())
		})
		logo.loadWithResult(url = imageHelper.getLogoImageUrl(mFolder, 900)) { loaded ->
			if (this@GenericFolderFragment.view !== root) return@loadWithResult
			logo.visibility = if (loaded) View.VISIBLE else View.INVISIBLE
			(mTitle.layoutParams as RelativeLayout.LayoutParams).apply {
				addRule(RelativeLayout.START_OF, if (loaded) logo.id else R.id.npBug)
				mTitle.layoutParams = this
			}
		}
	}

	override fun onDestroyView() {
		themeMusic?.dispose()
		themeMusic = null
		super.onDestroyView()
	}

	companion object {
		private val showSpecialViewTypes = setOf(
			BaseItemKind.COLLECTION_FOLDER,
			BaseItemKind.FOLDER,
			BaseItemKind.USER_VIEW,
			BaseItemKind.CHANNEL_FOLDER_ITEM,
		)
	}

	override fun setupQueries(rowLoader: RowLoader) {
		if ((mFolder.childCount == null || mFolder.childCount == 0) && !setOf(
				BaseItemKind.CHANNEL,
				BaseItemKind.CHANNEL_FOLDER_ITEM,
				BaseItemKind.USER_VIEW,
				BaseItemKind.COLLECTION_FOLDER,
			).contains(mFolder.type)
		) return

		if (showSpecialViewTypes.contains(mFolder.type)) {
			if (mFolder.type != BaseItemKind.CHANNEL_FOLDER_ITEM) {
				val resume = GetItemsRequest(
					fields = ItemRepository.itemFields,
					parentId = mFolder.id,
					limit = 50,
					filters = setOf(ItemFilter.IS_RESUMABLE),
					sortBy = setOf(ItemSortBy.DATE_PLAYED),
					sortOrder = setOf(SortOrder.DESCENDING),
				)
				mRows.add(BrowseRowDef(getString(R.string.lbl_continue_watching), resume, 0))
			}

			val latest = GetItemsRequest(
				fields = ItemRepository.itemFields,
				parentId = mFolder.id,
				limit = 50,
				filters = setOf(ItemFilter.IS_UNPLAYED),
				sortBy = setOf(ItemSortBy.DATE_CREATED),
				sortOrder = setOf(SortOrder.DESCENDING),
			)
			mRows.add(BrowseRowDef(getString(R.string.lbl_latest), latest, 0))
		}

		val byName = GetItemsRequest(
			fields = ItemRepository.itemFields,
			parentId = mFolder.id,
		)
		val header = when (mFolder.type) {
			BaseItemKind.SEASON -> mFolder.name
			else -> getString(R.string.lbl_by_name)
		}

		mRows.add(BrowseRowDef(header, byName, 100))

		if (mFolder.type == BaseItemKind.SEASON) {
			val specials = GetSpecialsRequest(mFolder.id)
			mRows.add(BrowseRowDef(getString(R.string.lbl_specials), specials))
		}

		rowLoader.loadRows(mRows)
	}
}
