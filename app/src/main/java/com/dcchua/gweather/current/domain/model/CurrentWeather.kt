package com.dcchua.gweather.current.domain.model

import com.google.android.gms.common.api.ResolvableApiException

sealed class CurrentWeather {
	data class FullData(
		val weather: Weather,
		val temperature: Double,
		val area: Area,
		val sunrise: Long,
		val sunset: Long,
	) : CurrentWeather() {
		sealed class Weather {
			data object Clear : Weather()
			data object Cloudy : Weather()
			data object Rain : Weather()
			data object Snow : Weather()
			data object ThunderStorm : Weather()
			data object Mist : Weather()
		}
		data class Area(val city: String, val country: String)
	}
	sealed class ErrorData : CurrentWeather() {
		data object GPSUnsupported : ErrorData()
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorData()
		data object PermissionRequired : ErrorData()
		data class LocationUnknown(val message: String) : ErrorData()
		data object NetworkError : ErrorData()
	}
	data object NoData : CurrentWeather()
}