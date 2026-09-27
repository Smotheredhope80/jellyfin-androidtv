package org.jellyfin.androidtv.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jellyfin.sdk.model.api.BaseItemDto

class HomePreviewViewModel : ViewModel() {
	private val _selectedItem = MutableStateFlow<BaseItemDto?>(null)

	val selectedItem: StateFlow<BaseItemDto?> = _selectedItem.asStateFlow()

	fun select(item: BaseItemDto?) {
		_selectedItem.value = item
	}
}
