package com.dcchua.gweather.current.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcchua.gweather.current.domain.usecase.CurrentWeatherDataProvider
import com.dcchua.gweather.core.util.time.getDurationToNextHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
	private val currentWeatherDataProvider: CurrentWeatherDataProvider,
	private val factory: CurrentWeatherUiStateFactory,
) : ViewModel() {

	private var getCurrentWeatherJob: Job? = null
	private var synchronizedHourlyCheck: Job? = null

	private val _isDayTime = MutableStateFlow(true)
	val isDayTime = _isDayTime.asStateFlow()

	val uiStream = combine(
		currentWeatherDataProvider.dataStream,
		isDayTime,
	) { currentWeather, isDayTime ->
		factory.toUiState(currentWeather = currentWeather, isDayTime = isDayTime)
	}.distinctUntilChanged()

	init {
		startSynchronizedHourlyCheck()
		getCurrentWeather()
	}

	fun getCurrentWeather() {
		if (getCurrentWeatherJob?.isActive == true) return
		getCurrentWeatherJob = viewModelScope.launch {
			currentWeatherDataProvider.getCurrentWeather()
		}
	}

	private fun startSynchronizedHourlyCheck() {
		if (synchronizedHourlyCheck?.isActive == true) return
		synchronizedHourlyCheck = viewModelScope.launch {
			while (true) {
				val now = LocalDateTime.now()
				_isDayTime.value = now.hour in 6..17
				delay(getDurationToNextHour(now = now))
			}
		}
	}

}