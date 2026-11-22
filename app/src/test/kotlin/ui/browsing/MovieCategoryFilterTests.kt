package org.jellyfin.androidtv.ui.browsing

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.request.GetItemsRequest
import java.util.UUID

class MovieCategoryFilterTests : FunSpec({
	val parentId = UUID.fromString("11111111-2222-3333-4444-555555555555")

	test("ALL clears tags, enforces MOVIE and recursive=true, and preserves parentId") {
		val base = GetItemsRequest(
			parentId = parentId,
			includeItemTypes = setOf(BaseItemKind.MOVIE, BaseItemKind.BOX_SET),
			recursive = false,
			tags = setOf("christmas"),
		)

		val filtered = MovieCategoryFilter.applyMovieCategoryFilter(base, MovieCategoryFilter.ALL)

		filtered.parentId shouldBe parentId
		filtered.includeItemTypes shouldBe setOf(BaseItemKind.MOVIE)
		filtered.recursive shouldBe true
		filtered.tags shouldBe null
	}

	test("seasonal categories apply expected tag and exclude BOX_SET") {
		val base = GetItemsRequest(parentId = parentId)

		val holiday = MovieCategoryFilter.applyMovieCategoryFilter(base, MovieCategoryFilter.HOLIDAY)
		holiday.parentId shouldBe parentId
		holiday.includeItemTypes shouldBe setOf(BaseItemKind.MOVIE)
		holiday.recursive shouldBe true
		holiday.tags shouldBe setOf("holiday")

		val christmas = MovieCategoryFilter.applyMovieCategoryFilter(base, MovieCategoryFilter.CHRISTMAS)
		christmas.parentId shouldBe parentId
		christmas.includeItemTypes shouldBe setOf(BaseItemKind.MOVIE)
		christmas.recursive shouldBe true
		christmas.tags shouldBe setOf("christmas")

		val halloween = MovieCategoryFilter.applyMovieCategoryFilter(base, MovieCategoryFilter.HALLOWEEN)
		halloween.parentId shouldBe parentId
		halloween.includeItemTypes shouldBe setOf(BaseItemKind.MOVIE)
		halloween.includeItemTypes.orEmpty() shouldNotContain BaseItemKind.BOX_SET
		halloween.recursive shouldBe true
		halloween.tags shouldBe setOf("halloween")
	}

	test("fromTag handles null, blank, whitespace, mixed case, and unknown tags") {
		MovieCategoryFilter.fromTag(null) shouldBe MovieCategoryFilter.ALL
		MovieCategoryFilter.fromTag("") shouldBe MovieCategoryFilter.ALL
		MovieCategoryFilter.fromTag("   ") shouldBe MovieCategoryFilter.ALL
		MovieCategoryFilter.fromTag("Holiday") shouldBe MovieCategoryFilter.HOLIDAY
		MovieCategoryFilter.fromTag("Christmas") shouldBe MovieCategoryFilter.CHRISTMAS
		MovieCategoryFilter.fromTag(" HALLOWEEN ") shouldBe MovieCategoryFilter.HALLOWEEN
		MovieCategoryFilter.fromTag("action") shouldBe MovieCategoryFilter.ALL
	}
})
