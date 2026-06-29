package com.dcchua.gweather.current.data.remote.transformer

import com.dcchua.gweather.current.data.remote.api.dto.CurrentWeatherDto
import com.dcchua.gweather.current.domain.model.CurrentWeather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CurrentWeatherTransformerTest {

    private val sut = CurrentWeatherTransformer()

    @Test
    fun `toDomain should map basic fields correctly`() {
        val dto = createDto(weatherId = 800)
        val result = sut.toDomain(dto)

        assertTrue(result is CurrentWeather.FullData)
        val data = result as CurrentWeather.FullData
        assertEquals(284.2, data.temperature)
        assertEquals("Taguig", data.area.city)
        assertEquals("PH", data.area.country)
        assertEquals(1726636384L, data.sunrise)
        assertEquals(1726680975L, data.sunset)
        assertEquals(CurrentWeather.FullData.Weather.Clear, data.weather)
    }

    @ParameterizedTest
    @MethodSource("weatherMappingProvider")
    fun `toDomain should map weather id correctly`(weatherId: Int, expectedWeather: CurrentWeather.FullData.Weather) {
        val dto = createDto(weatherId = weatherId)
        val result = sut.toDomain(dto)

        assertTrue(result is CurrentWeather.FullData)
        assertEquals(expectedWeather, (result as CurrentWeather.FullData).weather)
    }

    @Test
    fun `toDomain should return NetworkError when weather list is empty`() {
        val dto = createDto(weatherId = 800).copy(weather = emptyList())
        val result = sut.toDomain(dto)

        assertEquals(CurrentWeather.ErrorData.NetworkError, result)
    }

    companion object {
        @JvmStatic
        fun weatherMappingProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(200, CurrentWeather.FullData.Weather.ThunderStorm),
            Arguments.of(299, CurrentWeather.FullData.Weather.ThunderStorm),
            Arguments.of(300, CurrentWeather.FullData.Weather.Rain),
            Arguments.of(399, CurrentWeather.FullData.Weather.Rain),
            Arguments.of(500, CurrentWeather.FullData.Weather.Rain),
            Arguments.of(599, CurrentWeather.FullData.Weather.Rain),
            Arguments.of(600, CurrentWeather.FullData.Weather.Snow),
            Arguments.of(699, CurrentWeather.FullData.Weather.Snow),
            Arguments.of(701, CurrentWeather.FullData.Weather.Mist),
            Arguments.of(799, CurrentWeather.FullData.Weather.Mist),
            Arguments.of(800, CurrentWeather.FullData.Weather.Clear),
            Arguments.of(801, CurrentWeather.FullData.Weather.Cloudy),
            Arguments.of(804, CurrentWeather.FullData.Weather.Cloudy),
            Arguments.of(900, CurrentWeather.FullData.Weather.Clear), // else case
        )
    }

    private fun createDto(weatherId: Int) = CurrentWeatherDto(
        coord = CurrentWeatherDto.CoordDto(7.367, 45.133),
        weather = listOf(CurrentWeatherDto.WeatherDto(weatherId, "Main", "Description", "icon")),
        base = "stations",
        main = CurrentWeatherDto.MainDto(
            temp = 284.2,
            feelsLike = 282.93,
            tempMin = 283.06,
            tempMax = 286.82,
            pressure = 1021,
            humidity = 60
        ),
        visibility = 10000,
        wind = CurrentWeatherDto.WindDto(4.09, 121),
        clouds = CurrentWeatherDto.CloudsDto(83),
        dt = 1726660758,
        sys = CurrentWeatherDto.SysDto(
            country = "PH",
            sunrise = 1726636384,
            sunset = 1726680975
        ),
        timezone = 7200,
        id = 3165523,
        name = "Taguig",
        cod = 200
    )
}
