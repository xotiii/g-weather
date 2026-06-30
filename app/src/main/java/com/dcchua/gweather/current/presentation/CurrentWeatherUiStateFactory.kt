package com.dcchua.gweather.current.presentation

import com.dcchua.gweather.R
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
				temperature = currentWeather.temperature.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toString(),
				weather = getWeather(weather = currentWeather.weather, isDayTime = isDayTime),
				location = CurrentWeatherUiState.SuccessState.Location(
					city = currentWeather.area.city,
					country = currentWeather.area.country,
				),
				sunrise = toLocalTime(unixUtcSeconds = currentWeather.sunrise),
				sunset = toLocalTime(unixUtcSeconds = currentWeather.sunset),
			)

			CurrentWeather.NoData -> CurrentWeatherUiState.Loading
		}

	private fun getWeather(
		weather: CurrentWeather.FullData.Weather,
		isDayTime: Boolean,
	) = CurrentWeatherUiState.SuccessState.Weather(
		iconId = when (weather) {
			CurrentWeather.FullData.Weather.Clear -> if (isDayTime) R.drawable.clear_sun else R.drawable.clear_moon
			CurrentWeather.FullData.Weather.Cloudy -> if (isDayTime) R.drawable.cloudy_sun else R.drawable.cloudy_moon
			CurrentWeather.FullData.Weather.Mist -> if (isDayTime) R.drawable.cloudy_sun else R.drawable.cloudy_moon
			CurrentWeather.FullData.Weather.Rain -> R.drawable.rain
			CurrentWeather.FullData.Weather.Snow -> R.drawable.snow
			CurrentWeather.FullData.Weather.ThunderStorm -> R.drawable.storm
		},
		name = when (weather) {
			CurrentWeather.FullData.Weather.Clear -> R.string.current_weather_weather_clear
			CurrentWeather.FullData.Weather.Cloudy -> R.string.current_weather_weather_cloudy
			CurrentWeather.FullData.Weather.Mist -> R.string.current_weather_weather_mist
			CurrentWeather.FullData.Weather.Rain -> R.string.current_weather_weather_rain
			CurrentWeather.FullData.Weather.Snow -> R.string.current_weather_weather_snow
			CurrentWeather.FullData.Weather.ThunderStorm -> R.string.current_weather_weather_thunderstorm
		}
	)

}