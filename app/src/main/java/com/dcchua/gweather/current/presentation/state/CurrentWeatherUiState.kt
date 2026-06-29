package com.dcchua.gweather.current.presentation.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.gms.common.api.ResolvableApiException

sealed class CurrentWeatherUiState {

	data class SuccessState(
		val temperature: String,
		val weather: Weather,
		val location: Location,
		val sunrise: String,
		val sunset: String,
	) : CurrentWeatherUiState() {
		data class Weather(
			@param:DrawableRes val iconId: Int,
			@param:StringRes val name: Int,
		)

		data class Location(val city: String, val country: String)
	}

	sealed class ErrorState : CurrentWeatherUiState() {
		data object GPSUnsupported : ErrorState()
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorState()
		data object PermissionRequired : ErrorState()
		data class LocationUnknown(val message: String) : ErrorState()
		data object NetworkError : ErrorState()
	}

	data object Loading : CurrentWeatherUiState()

}