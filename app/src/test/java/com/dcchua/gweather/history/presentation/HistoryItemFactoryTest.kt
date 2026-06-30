package com.dcchua.gweather.history.presentation

import com.dcchua.gweather.R
import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.util.time.toLocalDate
import com.dcchua.gweather.core.util.time.toLocalTime
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class HistoryItemFactoryTest {

    private val sut = HistoryItemFactory()

    @BeforeEach
    fun setup() {
        mockkStatic("com.dcchua.gweather.core.util.time.TimeUtilKt")
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic("com.dcchua.gweather.core.util.time.TimeUtilKt")
    }

    @Test
    fun `toPresentation should map domain model correctly`() {
        val weather = Weather.History(
            weatherCondition = Weather.WeatherCondition.Clear,
            temperature = 25.54,
            area = Weather.Area("Taguig", "PH"),
            sunrise = 1000,
            sunset = 3000,
            timestamp = 2000000
        )

        every { toLocalDate(2000000L) } returns "June 26, 2026"
        every { toLocalTime(1000L) } returns "5:30 AM"
        every { toLocalTime(3000L) } returns "5:30 PM"

        val result = sut.toPresentation(weather)

        assertEquals("Taguig, PH", result.location)
        assertEquals("June 26, 2026", result.date)
        assertEquals("25.5", result.temperature)
        assertEquals("5:30 AM", result.sunrise)
        assertEquals("5:30 PM", result.sunset)
    }

    @ParameterizedTest
    @MethodSource("weatherMappingProvider")
    fun `toPresentation should map weather icons correctly`(
        condition: Weather.WeatherCondition,
        expectedIcon: Int
    ) {
        val weather = Weather.History(
            weatherCondition = condition,
            temperature = 20.0,
            area = Weather.Area("City", "Country"),
            sunrise = 1000,
            sunset = 3000,
            timestamp = 2000000
        )

        every { toLocalDate(any()) } returns ""
        every { toLocalTime(any()) } returns ""

        val result = sut.toPresentation(weather)

        assertEquals(expectedIcon, result.iconId)
    }

    companion object {
        @JvmStatic
        fun weatherMappingProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(Weather.WeatherCondition.Clear, R.drawable.clear_sun),
            Arguments.of(Weather.WeatherCondition.Cloudy, R.drawable.cloudy_sun),
            Arguments.of(Weather.WeatherCondition.Mist, R.drawable.cloudy_sun),
            Arguments.of(Weather.WeatherCondition.Rain, R.drawable.rain),
            Arguments.of(Weather.WeatherCondition.Snow, R.drawable.snow),
            Arguments.of(Weather.WeatherCondition.ThunderStorm, R.drawable.storm)
        )
    }
}
