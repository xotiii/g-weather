package com.dcchua.gweather.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcchua.gweather.core.domain.usecase.GetUser
import com.dcchua.gweather.history.domain.usecase.GetWeatherHistory
import com.dcchua.gweather.history.presentation.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
	getWeatherHistory: GetWeatherHistory,
	getUser: GetUser,
	factory: HistoryItemFactory,
) : ViewModel() {

	val historyList: StateFlow<List<HistoryItem>> = getWeatherHistory(getUser()?.id ?: -1L)
		.map { history ->
			history.map { factory.toPresentation(it) }
		}
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(),
			initialValue = emptyList()
		)

}
