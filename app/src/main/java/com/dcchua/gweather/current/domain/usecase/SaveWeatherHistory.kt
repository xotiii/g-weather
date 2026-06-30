package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.core.domain.repository.WeatherRepository
import com.dcchua.gweather.current.domain.model.CurrentWeather
import javax.inject.Inject

class SaveWeatherHistory @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(userId: Long, state: CurrentWeather.FullData) =
        weatherRepository.saveWeather(userId, state.weather)
}
