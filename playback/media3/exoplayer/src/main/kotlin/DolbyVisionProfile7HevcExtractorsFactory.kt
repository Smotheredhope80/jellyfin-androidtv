package org.jellyfin.playback.media3.exoplayer

import android.net.Uri
import androidx.media3.common.Format
import androidx.media3.common.MimeTypes
import androidx.media3.extractor.Extractor
import androidx.media3.extractor.ExtractorOutput
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.TrackOutput
import timber.log.Timber

/**
 * Exposes the HDR10-compatible HEVC base layer of Dolby Vision Profile 7 to
 * Media3. NVIDIA SHIELD's decoder is able to decode that base layer, but it
 * does not advertise support for Media3's `video/dolby-vision` Profile 7
 * subtype and Media3 otherwise rejects the track before creating a decoder.
 */
internal class DolbyVisionProfile7HevcExtractorsFactory(
	private val delegate: ExtractorsFactory,
) : ExtractorsFactory {
	override fun createExtractors(): Array<Extractor> =
		delegate.createExtractors().map(::DolbyVisionProfile7HevcExtractor).toTypedArray()

	override fun createExtractors(
		uri: Uri,
		responseHeaders: Map<String, List<String>>,
	): Array<Extractor> = delegate.createExtractors(uri, responseHeaders)
		.map(::DolbyVisionProfile7HevcExtractor)
		.toTypedArray()
}

private class DolbyVisionProfile7HevcExtractor(
	private val delegate: Extractor,
) : Extractor by delegate {
	override fun init(output: ExtractorOutput) {
		delegate.init(DolbyVisionProfile7HevcExtractorOutput(output))
	}
}

private class DolbyVisionProfile7HevcExtractorOutput(
	private val delegate: ExtractorOutput,
) : ExtractorOutput by delegate {
	override fun track(id: Int, type: Int): TrackOutput =
		DolbyVisionProfile7HevcTrackOutput(delegate.track(id, type))
}

private class DolbyVisionProfile7HevcTrackOutput(
	private val delegate: TrackOutput,
) : TrackOutput by delegate {
	override fun format(format: Format) {
		val codecs = format.codecs.orEmpty()
		val isProfile7 = format.sampleMimeType == MimeTypes.VIDEO_DOLBY_VISION &&
			(codecs.startsWith("dvhe.07", ignoreCase = true) || codecs.startsWith("dvh1.07", ignoreCase = true))

		if (isProfile7) {
			Timber.i("Mapping Dolby Vision Profile 7 to HEVC base-layer playback (%s)", codecs)
			delegate.format(
				format.buildUpon()
					.setSampleMimeType(MimeTypes.VIDEO_H265)
					.setCodecs(null)
					.build()
			)
		} else {
			delegate.format(format)
		}
	}
}
