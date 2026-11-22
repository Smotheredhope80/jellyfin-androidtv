package org.jellyfin.androidtv.ui.browsing

import androidx.annotation.StringRes
import org.jellyfin.androidtv.R
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.request.GetItemsRequest
import java.util.Locale

enum class MovieCategoryFilter(
	val tag: String?,
	@param:StringRes val labelRes: Int,
) {
	ALL(null, R.string.lbl_all_movies),
	HOLIDAY("holiday", R.string.lbl_holiday),
	CHRISTMAS("christmas", R.string.lbl_christmas),
	HALLOWEEN("halloween", R.string.lbl_halloween);

	companion object {
		@JvmStatic
		fun fromTag(tag: String?): MovieCategoryFilter {
			val normalized = tag?.trim()?.lowercase(Locale.ROOT)
			if (normalized.isNullOrEmpty()) return ALL
			return entries.firstOrNull { it.tag == normalized } ?: ALL
		}

		@JvmStatic
		fun applyMovieCategoryFilter(
			request: GetItemsRequest,
			category: MovieCategoryFilter,
		): GetItemsRequest = request.copy(
			includeItemTypes = setOf(BaseItemKind.MOVIE),
			recursive = true,
			tags = category.tag?.let { setOf(it) },
		)
	}
}
