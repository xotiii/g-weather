package com.dcchua.gweather.current.presentation

import com.dcchua.gweather.R
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import com.google.android.gms.common.api.ResolvableApiException
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CurrentWeatherUiStateFactoryTest {

    private val sut = CurrentWeatherUiStateFactory()

    @Test
    fun `toUiState should return Loading when domain model is NoData`() {
        val result = sut.toUiState(CurrentWeather.NoData, true)
        assertEquals(CurrentWeatherUiState.Loading, result)
    }

    @Test
    fun `toUiState should return GPSUnsupported when domain model is GPSUnsupported`() {
        val result = sut.toUiState(CurrentWeather.ErrorData.GPSUnsupported, true)
        assertEquals(CurrentWeatherUiState.ErrorState.GPSUnsupported, result)
    }

    @Test
    fun `toUiState should return LocationUnknown when domain model is LocationUnknown`() {
        val result = sut.toUiState(CurrentWeather.ErrorData.LocationUnknown("Error message"), true)
        assertTrue(result is CurrentWeatherUiState.ErrorState.LocationUnknown)
        assertEquals("Error message", (result as CurrentWeatherUiState.ErrorState.LocationUnknown).message)
    }

    @Test
    fun `toUiState should return NetworkError when domain model is NetworkError`() {
        val result = sut.toUiState(CurrentWeather.ErrorData.NetworkError, true)
        assertEquals(CurrentWeatherUiState.ErrorState.NetworkError, result)
    }

    @Test
    fun `toUiState should return PermissionRequired when domain model is PermissionRequired`() {
        val result = sut.toUiState(CurrentWeather.ErrorData.PermissionRequired, true)
        assertEquals(CurrentWeatherUiState.ErrorState.PermissionRequired, result)
    }

    @Test
    fun `toUiState should return RequiresResolution when domain model is RequiresResolution`() {
        val exception = mockk<ResolvableApiException>()
        val result = sut.toUiState(CurrentWeather.ErrorData.RequiresResolution(exception), true)
        assertTrue(result is CurrentWeatherUiState.ErrorState.RequiresResolution)
        assertEquals(exception, (result as CurrentWeatherUiState.ErrorState.RequiresResolution).exception)
    }

    @Test
    fun `toUiState should map FullData correctly and round temperature`() {
        val fullData = CurrentWeather.FullData(
            weather = CurrentWeather.FullData.Weather.Clear,
            temperature = 25.46,
            area = CurrentWeather.FullData.Area("Turin", "IT"),
            sunrise = 1726636384,
            sunset = 1726680975
        )

        val result = sut.toUiState(fullData, isDayTime = true)

        assertTrue(result is CurrentWeatherUiState.SuccessState)
        val successState = result as CurrentWeatherUiState.SuccessState
        assertEquals("25.5", successState.temperature)
        assertEquals("Turin", successState.location.city)
        assertEquals("IT", successState.location.country)
        assertEquals(R.drawable.clear_sun, successState.weather.iconId)
        assertEquals(R.string.current_weather_weather_clear, successState.weather.name)
        // Check if time conversion was called (basic check as output depends on timezone)
        assertTrue(successState.sunrise.isNotEmpty())
        assertTrue(successState.sunset.isNotEmpty())
    }

    @ParameterizedTest
    @MethodSource("weatherMappingProvider")
    fun `toUiState should map weather icons correctly based on time of day`(
        weather: CurrentWeather.FullData.Weather,
        isDayTime: Boolean,
        expectedIcon: Int
    ) {
        val fullData = CurrentWeather.FullData(
            weather = weather,
            temperature = 20.0,
            area = CurrentWeather.FullData.Area("City", "Country"),
            sunrise = 0,
            sunset = 0
        )

        val result = sut.toUiState(fullData, isDayTime)

        assertTrue(result is CurrentWeatherUiState.SuccessState)
        assertEquals(expectedIcon, (result as CurrentWeatherUiState.SuccessState).weather.iconId)
    }

    companion object {
        @JvmStatic
        fun weatherMappingProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(CurrentWeather.FullData.Weather.Clear, true, R.drawable.clear_sun),
            Arguments.of(CurrentWeather.FullData.Weather.Clear, false, R.drawable.clear_moon),
            Arguments.of(CurrentWeather.FullData.Weather.Cloudy, true, R.drawable.cloudy_sun),
            Arguments.of(CurrentWeather.FullData.Weather.Cloudy, false, R.drawable.cloudy_moon),
            Arguments.of(CurrentWeather.FullData.Weather.Mist, true, R.drawable.cloudy_sun),
            Arguments.of(CurrentWeather.FullData.Weather.Mist, false, R.drawable.cloudy_moon),
            Arguments.of(CurrentWeather.FullData.Weather.Rain, true, R.drawable.rain),
            Arguments.of(CurrentWeather.FullData.Weather.Rain, false, R.drawable.rain),
            Arguments.of(CurrentWeather.FullData.Weather.Snow, true, R.drawable.snow),
            Arguments.of(CurrentWeather.FullData.Weather.Snow, false, R.drawable.snow),
            Arguments.of(CurrentWeather.FullData.Weather.ThunderStorm, true, R.drawable.storm),
            Arguments.of(CurrentWeather.FullData.Weather.ThunderStorm, false, R.drawable.storm)
        )
    }
}
