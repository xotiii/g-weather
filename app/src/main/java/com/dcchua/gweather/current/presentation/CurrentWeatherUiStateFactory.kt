package com.dcchua.gweather.current.presentation

import com.dcchua.gweather.R
import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import com.dcchua.gweather.core.util.time.toLocalTime
import java.math.RoundingMode
import javax.inject.Inject

class CurrentWeatherUiStateFactory @Inject constructor() {

	fun toUiState(currentWeather: CurrentWeather, isDayTime: Boolean): CurrentWeatherUiState =
		when (currentWeather) {
			CurrentWeather.ErrorData.GPSUnsupported -> CurrentWeatherUiState.ErrorState.GPSUnsupported
			is CurrentWeather.ErrorData.LocationUnknown ->
				CurrentWeatherUiState.ErrorState.LocationUnknown(message = currentWeather.message)

			CurrentWeather.ErrorData.NetworkError -> CurrentWeatherUiState.ErrorState.NetworkError
			CurrentWeather.ErrorData.PermissionRequired -> CurrentWeatherUiState.ErrorState.PermissionRequired
			is CurrentWeather.ErrorData.RequiresResolution ->
				CurrentWeatherUiState.ErrorState.RequiresResolution(exception = currentWeather.exception)

			is CurrentWeather.FullData -> CurrentWeatherUiState.SuccessState(
				temperature = currentWeather.weather.temperature.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toString(),
				weather = getWeather(weather = currentWeather.weather.weatherCondition, isDayTime = isDayTime),
				location = CurrentWeatherUiState.SuccessState.Location(
					city = currentWeather.weather.area.city,
					country = currentWeather.weather.area.country,
				),
				sunrise = toLocalTime(unixUtcSeconds = currentWeather.weather.sunrise),
				sunset = toLocalTime(unixUtcSeconds = currentWeather.weather.sunset),
			)

			CurrentWeather.NoData -> CurrentWeatherUiState.Loading
		}

	private fun getWeather(
		weather: Weather.WeatherCondition,
		isDayTime: Boolean,
	) = CurrentWeatherUiState.SuccessState.Weather(
		iconId = when (weather) {
			Weather.WeatherCondition.Clear -> if (isDayTime) R.drawable.clear_sun else R.drawable.clear_moon
			Weather.WeatherCondition.Cloudy -> if (isDayTime) R.drawable.cloudy_sun else R.drawable.cloudy_moon
			Weather.WeatherCondition.Mist -> if (isDayTime) R.drawable.cloudy_sun else R.drawable.cloudy_moon
			Weather.WeatherCondition.Rain -> R.drawable.rain
			Weather.WeatherCondition.Snow -> R.drawable.snow
			Weather.WeatherCondition.ThunderStorm -> R.drawable.storm
		},
		name = when (weather) {
			Weather.WeatherCondition.Clear -> R.string.current_weather_weather_clear
			Weather.WeatherCondition.Cloudy -> R.string.current_weather_weather_cloudy
			Weather.WeatherCondition.Mist -> R.string.current_weather_weather_mist
			Weather.WeatherCondition.Rain -> R.string.current_weather_weather_rain
			Weather.WeatherCondition.Snow -> R.string.current_weather_weather_snow
			Weather.WeatherCondition.ThunderStorm -> R.string.current_weather_weather_thunderstorm
		}
	)

}