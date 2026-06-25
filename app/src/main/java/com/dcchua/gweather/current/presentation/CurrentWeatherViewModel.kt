package com.dcchua.gweather.current.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcchua.gweather.current.domain.model.Location
import com.dcchua.gweather.current.domain.usecase.LocationDataProvider
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
	private val locationDataProvider: LocationDataProvider,
) : ViewModel() {

	init {
		loadLocation()
	}

	val uiStream = locationDataProvider.getDataStream().map {
		when (it) {
			Location.ErrorData.GPSUnsupported -> CurrentWeatherUiState.ErrorState.GPSUnsupported
			Location.ErrorData.PermissionRequired -> CurrentWeatherUiState.ErrorState.PermissionRequired
			is Location.ErrorData.RequiresResolution -> CurrentWeatherUiState.ErrorState.RequiresResolution(it.exception)
			is Location.ErrorData.Unknown -> CurrentWeatherUiState.ErrorState.Unknown(it.message)
			is Location.FullData -> CurrentWeatherUiState.SuccessState.DayTime(it.longitude.toString(), it.latitude.toString())
			Location.NoData -> CurrentWeatherUiState.Loading
		}
	}

	fun loadLocation() {
		viewModelScope.launch {
			locationDataProvider.getLocationData()
		}
	}

}