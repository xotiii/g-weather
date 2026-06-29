package com.dcchua.gweather.current.domain

import com.dcchua.gweather.current.domain.model.CurrentWeather
import kotlinx.coroutines.flow.StateFlow

interface CurrentWeatherRepository {

	val currentWeatherDataStream: StateFlow<CurrentWeather>

	suspend fun fetchWeather(longitude: Double, latitude: Double, apiKey: String)

}