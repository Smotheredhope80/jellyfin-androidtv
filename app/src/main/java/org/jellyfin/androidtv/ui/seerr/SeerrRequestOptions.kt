package org.jellyfin.androidtv.ui.seerr

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.seerr.SeerrMediaType
import org.jellyfin.androidtv.data.seerr.SeerrServiceDetails
import org.jellyfin.androidtv.data.seerr.SeerrTitle
import org.jellyfin.androidtv.data.seerr.formatFreeSpace
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.base.button.Button
import org.jellyfin.androidtv.ui.base.form.Checkbox
import org.jellyfin.androidtv.ui.base.form.RadioButton
import org.jellyfin.androidtv.ui.base.list.ListButton

private val RowWidth = Modifier.fillMaxWidth(0.6f)

/**
 * Full-screen request options for users allowed to choose server, profile, folder and tags. It replaces
 * the details content while open; Back closes a choice list first, then the panel.
 */
@Composable
internal fun SeerrRequestOptionsPanel(
	title: SeerrTitle,
	options: SeerrOptionsState,
	requesting: Boolean,
	seasonCount: Int,
	viewModel: SeerrDetailsViewModel,
) {
	val firstFocus = remember { FocusRequester() }
	val details = options.details
	val picker = options.picker

	// Focus the first control whenever the panel, its loading state or the open choice list changes
	LaunchedEffect(picker, options.loading, options.error, options.ready) {
		runCatching { firstFocus.requestFocus() }
	}

	LazyColumn(
		contentPadding = PaddingValues(horizontal = 48.dp, vertical = 36.dp),
		verticalArrangement = Arrangement.spacedBy(6.dp),
		modifier = Modifier.fillMaxSize().focusRestorer().focusGroup(),
	) {
		item(key = "heading") {
			Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 12.dp)) {
				Text(stringResource(R.string.seerr_request_options), color = SeerrAccent, fontSize = 14.sp)
				Text(title.title, color = Color.White, fontSize = 26.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
			}
		}

		when {
			options.loading -> item(key = "loading") {
				// Focusable so Back still works while loading
				Button(onClick = {}, modifier = Modifier.focusRequester(firstFocus)) { Text(stringResource(R.string.loading)) }
			}

			!options.ready -> item(key = "unavailable") {
				Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
					Text(
						options.error?.let { stringResource(R.string.seerr_options_failed, it) } ?: stringResource(R.string.seerr_options_no_servers),
						color = Color.LightGray,
						fontSize = 15.sp,
					)
					Button(
						onClick = viewModel::requestWithDefaults,
						enabled = !requesting,
						modifier = Modifier.focusRequester(firstFocus),
					) { Text(stringResource(R.string.seerr_request_with_defaults)) }
				}
			}

			details != null && picker != null -> pickerItems(picker, options, details, viewModel, firstFocus)

			details != null -> optionRows(title, options, details, requesting, seasonCount, viewModel, firstFocus)
		}
	}
}

private fun LazyListScope.optionRows(
	title: SeerrTitle,
	options: SeerrOptionsState,
	details: SeerrServiceDetails,
	requesting: Boolean,
	seasonCount: Int,
	viewModel: SeerrDetailsViewModel,
	firstFocus: FocusRequester,
) {
	val selected = options.selected ?: return
	var focusAssigned = false
	fun first(): Modifier = if (focusAssigned) RowWidth else RowWidth.focusRequester(firstFocus).also { focusAssigned = true }

	if (options.serverFellBack) item(key = "fell-back") {
		Text(stringResource(R.string.seerr_options_fell_back), color = SeerrAccent, fontSize = 13.sp)
	}

	if (options.servers.size > 1) {
		val modifier = first()
		item(key = "server") {
			OptionRow(
				label = stringResource(R.string.seerr_option_server),
				value = details.server.name ?: stringResource(R.string.seerr_option_unknown, details.server.id),
				enabled = !requesting,
				onClick = { viewModel.openPicker(SeerrOptionPicker.SERVER) },
				modifier = modifier,
			)
		}
	}

	val profileModifier = first()
	item(key = "profile") {
		OptionRow(
			label = stringResource(R.string.seerr_option_profile),
			value = selected.profileId?.let { id -> details.profiles.firstOrNull { it.id == id }?.name ?: stringResource(R.string.seerr_option_unknown, id) }
				?: stringResource(R.string.seerr_option_none),
			enabled = !requesting,
			onClick = { viewModel.openPicker(SeerrOptionPicker.PROFILE) },
			modifier = profileModifier,
		)
	}

	item(key = "folder") {
		val free = details.rootFolders.firstOrNull { it.path == selected.rootFolder }?.freeSpace?.let(::formatFreeSpace)
		OptionRow(
			label = stringResource(R.string.seerr_option_folder),
			value = listOfNotNull(
				selected.rootFolder ?: stringResource(R.string.seerr_option_none),
				free?.let { stringResource(R.string.seerr_folder_free, it) },
			).joinToString(SEERR_SEPARATOR),
			enabled = !requesting,
			onClick = { viewModel.openPicker(SeerrOptionPicker.FOLDER) },
			modifier = RowWidth,
		)
	}

	// Only Sonarr v3 has language profiles; v4 returns null
	val languageProfiles = details.languageProfiles
	if (!languageProfiles.isNullOrEmpty()) item(key = "language") {
		OptionRow(
			label = stringResource(R.string.seerr_option_language),
			value = selected.languageProfileId?.let { id -> languageProfiles.firstOrNull { it.id == id }?.name ?: stringResource(R.string.seerr_option_unknown, id) }
				?: stringResource(R.string.seerr_option_none),
			enabled = !requesting,
			onClick = { viewModel.openPicker(SeerrOptionPicker.LANGUAGE) },
			modifier = RowWidth,
		)
	}

	if (details.tags.isNotEmpty()) item(key = "tags") {
		val labels = details.tags.filter { it.id in selected.tags }.mapNotNull { it.label }
		OptionRow(
			label = stringResource(R.string.seerr_option_tags),
			value = labels.takeIf { it.isNotEmpty() }?.joinToString(", ") ?: stringResource(R.string.seerr_option_none),
			enabled = !requesting,
			onClick = { viewModel.openPicker(SeerrOptionPicker.TAGS) },
			modifier = RowWidth,
		)
	}

	item(key = "actions") {
		Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
			Button(onClick = viewModel::confirmOptions, enabled = !requesting) {
				Text(
					when {
						requesting -> stringResource(R.string.seerr_requesting)
						title.type == SeerrMediaType.TV -> pluralStringResource(R.plurals.seerr_request_seasons, seasonCount, seasonCount)
						else -> stringResource(R.string.seerr_request)
					}
				)
			}
			Button(onClick = viewModel::closeOptions, enabled = !requesting) {
				Text(stringResource(R.string.lbl_cancel))
			}
		}
	}
}

private fun LazyListScope.pickerItems(
	picker: SeerrOptionPicker,
	options: SeerrOptionsState,
	details: SeerrServiceDetails,
	viewModel: SeerrDetailsViewModel,
	firstFocus: FocusRequester,
) {
	val selected = options.selected ?: return
	val defaults = options.defaults ?: return

	data class Choice(val key: String, val name: String?, val fallbackId: Int, val isDefault: Boolean, val checked: Boolean, val caption: String?, val onClick: () -> Unit)

	val choices = when (picker) {
		SeerrOptionPicker.SERVER -> options.servers.map { server ->
			Choice("server-${server.id}", server.name, server.id, server.id == options.defaultServerId, server.id == selected.serverId, null) { viewModel.selectServer(server.id) }
		}

		SeerrOptionPicker.PROFILE -> details.profiles.map { profile ->
			Choice("profile-${profile.id}", profile.name, profile.id, profile.id == defaults.profileId, profile.id == selected.profileId, null) { viewModel.selectProfile(profile.id) }
		}

		SeerrOptionPicker.FOLDER -> details.rootFolders.mapIndexed { index, folder ->
			Choice("folder-$index", folder.path, index, folder.path == defaults.rootFolder, folder.path == selected.rootFolder, formatFreeSpace(folder.freeSpace)) { viewModel.selectFolder(folder.path) }
		}

		SeerrOptionPicker.LANGUAGE -> details.languageProfiles.orEmpty().map { profile ->
			Choice("language-${profile.id}", profile.name, profile.id, profile.id == defaults.languageProfileId, profile.id == selected.languageProfileId, null) { viewModel.selectLanguageProfile(profile.id) }
		}

		SeerrOptionPicker.TAGS -> details.tags.map { tag ->
			Choice("tag-${tag.id}", tag.label, tag.id, tag.id in defaults.tags, tag.id in selected.tags, null) { viewModel.toggleTag(tag.id) }
		}
	}
	val multiSelect = picker == SeerrOptionPicker.TAGS

	item(key = "picker-heading") {
		Text(
			stringResource(
				when (picker) {
					SeerrOptionPicker.SERVER -> R.string.seerr_option_server
					SeerrOptionPicker.PROFILE -> R.string.seerr_option_profile
					SeerrOptionPicker.FOLDER -> R.string.seerr_option_folder
					SeerrOptionPicker.LANGUAGE -> R.string.seerr_option_language
					SeerrOptionPicker.TAGS -> R.string.seerr_option_tags
				}
			),
			color = Color.White,
			fontSize = 18.sp,
		)
	}

	// Focus starts on the current choice so a quick OK keeps it
	val focusIndex = choices.indexOfFirst { it.checked }.coerceAtLeast(0)
	items(choices.withIndex().toList(), key = { it.value.key }) { (index, choice) ->
		val name = choice.name ?: stringResource(R.string.seerr_option_unknown, choice.fallbackId)
		ListButton(
			onClick = choice.onClick,
			headingContent = { Text(if (choice.isDefault) stringResource(R.string.seerr_option_default, name) else name) },
			captionContent = if (choice.caption != null) {
			{ Text(stringResource(R.string.seerr_folder_free, choice.caption)) }
		} else null,
			trailingContent = { if (multiSelect) Checkbox(checked = choice.checked) else RadioButton(checked = choice.checked) },
			modifier = if (index == focusIndex) RowWidth.focusRequester(firstFocus) else RowWidth,
		)
	}

	if (multiSelect || choices.isEmpty()) item(key = "picker-done") {
		Button(
			onClick = viewModel::closePicker,
			modifier = Modifier.padding(top = 12.dp).then(if (choices.isEmpty()) Modifier.focusRequester(firstFocus) else Modifier),
		) { Text(stringResource(R.string.seerr_option_done)) }
	}
}

@Composable
private fun OptionRow(
	label: String,
	value: String,
	enabled: Boolean,
	onClick: () -> Unit,
	modifier: Modifier,
) {
	ListButton(
		onClick = onClick,
		enabled = enabled,
		headingContent = { Text(label) },
		captionContent = { Text(value, maxLines = 1, overflow = TextOverflow.Ellipsis) },
		modifier = modifier,
	)
}
