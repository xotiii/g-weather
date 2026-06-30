package com.dcchua.gweather.history.domain.usecase

import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.domain.repository.WeatherRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetWeatherHistoryTest {

    private lateinit var sut: GetWeatherHistory

    @RelaxedMockK
    private lateinit var weatherRepository: WeatherRepository

    @BeforeEach
    fun setup() {
        sut = GetWeatherHistory(weatherRepository)
    }

    @Test
    fun `invoke should return history from repository`() {
        val userId = 1L
        val historyListFlow = flowOf(listOf(mockk<Weather.History>()))
        every { weatherRepository.getWeatherHistory(userId) } returns historyListFlow

        val result = sut(userId)

        assertEquals(historyListFlow, result) // Note: this comparison might fail if flowOf is not same instance, but usually fine for simple tests or use testCollector
    }
}
