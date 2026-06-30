package com.dcchua.gweather.history.domain.usecase

import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherHistory @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(userId: Long): Flow<List<Weather.History>> {
        return weatherRepository.getWeatherHistory(userId)
    }
}
