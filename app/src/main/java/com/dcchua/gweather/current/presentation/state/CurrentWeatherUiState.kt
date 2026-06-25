package com.dcchua.gweather.current.presentation.state

import com.google.android.gms.common.api.ResolvableApiException

sealed interface CurrentWeatherUiState {

	sealed interface SuccessState : CurrentWeatherUiState {
		val temperature: String
		val weather: String

		data class DayTime(
			override val temperature: String,
			override val weather: String
		) : SuccessState

		data class NightTime(
			override val temperature: String,
			override val weather: String,
		) : SuccessState
	}

	sealed interface ErrorState : CurrentWeatherUiState {
		data object GPSUnsupported : ErrorState
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorState
		data object PermissionRequired : ErrorState
		data class Unknown(val message: String) : ErrorState
	}

	data object Loading : CurrentWeatherUiState

}