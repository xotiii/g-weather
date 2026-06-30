package com.dcchua.gweather.core.domain.model

sealed class Weather {
	abstract val weatherCondition: WeatherCondition
	abstract val temperature: Double
	abstract val area: Area
	abstract val sunrise: Long
	abstract val sunset: Long

	data class Current(
		override val weatherCondition: WeatherCondition,
		override val temperature: Double,
		override val area: Area,
		override val sunrise: Long,
		override val sunset: Long,
	) : Weather()

	data class History(
		override val weatherCondition: WeatherCondition,
		override val temperature: Double,
		override val area: Area,
		override val sunrise: Long,
		override val sunset: Long,
		val timestamp: Long,
	) : Weather()

	sealed class WeatherCondition {
		data object Clear : WeatherCondition()
		data object Cloudy : WeatherCondition()
		data object Rain : WeatherCondition()
		data object Snow : WeatherCondition()
		data object ThunderStorm : WeatherCondition()
		data object Mist : WeatherCondition()
	}

	data class Area(val city: String, val country: String)
}
