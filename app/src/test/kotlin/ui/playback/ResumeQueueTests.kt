package org.jellyfin.androidtv.ui.playback

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.every
import org.jellyfin.androidtv.ui.playback.rewrite.RewriteMediaManager
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.MediaType
import org.jellyfin.sdk.model.api.UserItemDataDto
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ResumeQueueTests : FunSpec({
	val api = mockk<ApiClient>()
	val savedProgress = mockk<UserItemDataDto>(relaxed = true) {
		every { playbackPositionTicks } returns 4_800_000_000L
	}
	fun movie() = BaseItemDto(
		id = UUID.randomUUID(), type = BaseItemKind.MOVIE, mediaType = MediaType.VIDEO,
		userData = savedProgress,
	)

	test("resume survives deferred queue loading and applies only to the first movie") {
		val supplier = RewriteMediaManager.BaseItemQueueSupplier(api, listOf(movie(), movie()), false, 42.minutes)
		val first = requireNotNull(supplier.getItem(0))
		first.initialPosition shouldBe 42.minutes
		first.consumeInitialPosition() shouldBe 42.minutes
		first.consumeInitialPosition() shouldBe null
		requireNotNull(supplier.getItem(1)).consumeInitialPosition() shouldBe null
	}

	test("play from beginning retains an explicit zero despite saved progress") {
		val supplier = RewriteMediaManager.BaseItemQueueSupplier(api, listOf(movie()), false, Duration.ZERO)
		requireNotNull(supplier.getItem(0)).consumeInitialPosition() shouldBe Duration.ZERO
	}

	test("preroll before the beginning is clamped to zero") {
		val supplier = RewriteMediaManager.BaseItemQueueSupplier(api, listOf(movie()), false, (-10).seconds)
		requireNotNull(supplier.getItem(0)).consumeInitialPosition() shouldBe Duration.ZERO
	}

	test("ordinary queues do not gain a resume seek") {
		val supplier = RewriteMediaManager.BaseItemQueueSupplier(api, listOf(movie()), true)
		requireNotNull(supplier.getItem(0)).consumeInitialPosition() shouldBe null
		supplier.getItem(1) shouldBe null
	}
})
