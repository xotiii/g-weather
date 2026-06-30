package com.dcchua.gweather.core.domain.repository

import com.dcchua.gweather.core.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun saveWeather(userId: Long, weather: Weather.Current)
    fun getWeatherHistory(userId: Long): Flow<List<Weather.History>>
}
