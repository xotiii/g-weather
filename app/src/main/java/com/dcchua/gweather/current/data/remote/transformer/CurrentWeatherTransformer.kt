package com.dcchua.gweather.current.data.remote.transformer

import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.current.data.remote.api.dto.CurrentWeatherDto
import com.dcchua.gweather.current.domain.model.CurrentWeather
import javax.inject.Inject

class CurrentWeatherTransformer @Inject constructor() {

	fun toDomain(dto: CurrentWeatherDto): CurrentWeather =
		dto.weather.firstOrNull()?.let {
			CurrentWeather.FullData(
				weather = Weather.Current(
					mapWeather(it.id),
					temperature = dto.main.temp,
					area = Weather.Area(
						city = dto.name,
						country = dto.sys.country,
					),
					sunrise = dto.sys.sunrise,
					sunset = dto.sys.sunset,
				)
			)
		} ?: CurrentWeather.ErrorData.NetworkError

	private fun mapWeather(id: Int?): Weather.WeatherCondition {
		return when (id) {
			in 200..299 -> Weather.WeatherCondition.ThunderStorm
			in 300..399 -> Weather.WeatherCondition.Rain
			in 500..599 -> Weather.WeatherCondition.Rain
			in 600..699 -> Weather.WeatherCondition.Snow
			in 700..799 -> Weather.WeatherCondition.Mist
			800 -> Weather.WeatherCondition.Clear
			in 801..804 -> Weather.WeatherCondition.Cloudy
			else -> Weather.WeatherCondition.Clear
		}
	}
}
