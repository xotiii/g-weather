package com.dcchua.gweather.current.data.remote.transformer

import com.dcchua.gweather.current.data.remote.api.dto.CurrentWeatherDto
import com.dcchua.gweather.current.domain.model.CurrentWeather
import javax.inject.Inject

class CurrentWeatherTransformer @Inject constructor() {

    fun toDomain(dto: CurrentWeatherDto): CurrentWeather =
        dto.weather.firstOrNull()?.let {
            CurrentWeather.FullData(
                weather = mapWeather(it.id),
                temperature = dto.main.temp,
                area = CurrentWeather.FullData.Area(
                    city = dto.name,
                    country = dto.sys.country,
                ),
                sunrise = dto.sys.sunrise,
                sunset = dto.sys.sunset,
            )
        } ?: CurrentWeather.ErrorData.NetworkError

    private fun mapWeather(id: Int?): CurrentWeather.FullData.Weather {
        return when (id) {
            in 200..299 -> CurrentWeather.FullData.Weather.ThunderStorm
            in 300..399 -> CurrentWeather.FullData.Weather.Rain
            in 500..599 -> CurrentWeather.FullData.Weather.Rain
            in 600..699 -> CurrentWeather.FullData.Weather.Snow
            in 700..799 -> CurrentWeather.FullData.Weather.Mist
            800 -> CurrentWeather.FullData.Weather.Clear
            in 801..804 -> CurrentWeather.FullData.Weather.Cloudy
            else -> CurrentWeather.FullData.Weather.Clear
        }
    }
}
