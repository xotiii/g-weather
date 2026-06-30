package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.domain.repository.WeatherRepository
import com.dcchua.gweather.current.domain.model.CurrentWeather
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class SaveWeatherHistoryTest {

    private lateinit var sut: SaveWeatherHistory

    @RelaxedMockK
    private lateinit var weatherRepository: WeatherRepository

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        sut = SaveWeatherHistory(weatherRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call saveWeather in repository`() = runTest {
        val userId = 1L
        val weather = mockk<Weather.Current>()
        val state = CurrentWeather.FullData(weather = weather)

        sut(userId, state)

        coVerify { weatherRepository.saveWeather(userId, weather) }
    }
}
