package com.dcchua.gweather.current.domain.model

import com.dcchua.gweather.core.domain.model.Weather
import com.google.android.gms.common.api.ResolvableApiException

sealed class CurrentWeather {
	data class FullData(
		val weather: Weather.Current,
	) : CurrentWeather()
	sealed class ErrorData : CurrentWeather() {
		data object GPSUnsupported : ErrorData()
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorData()
		data object PermissionRequired : ErrorData()
		data class LocationUnknown(val message: String) : ErrorData()
		data object NetworkError : ErrorData()
	}
	data object NoData : CurrentWeather()
}