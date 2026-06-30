package com.dcchua.gweather.core.data.repository

import com.dcchua.gweather.core.data.local.dao.WeatherDao
import com.dcchua.gweather.core.data.local.transformer.WeatherTransformer
import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val transformer: WeatherTransformer
) : WeatherRepository {

    override suspend fun saveWeather(userId: Long, weather: Weather.Current) {
        weatherDao.insertWeather(transformer.toEntity(userId, weather))
    }

    override fun getWeatherHistory(userId: Long): Flow<List<Weather.History>> {
        return weatherDao.getWeatherHistory(userId).map { entities ->
            entities.map { transformer.toDomain(it) }
        }
    }
}
