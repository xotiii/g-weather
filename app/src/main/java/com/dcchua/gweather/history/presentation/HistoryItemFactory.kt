package com.dcchua.gweather.history.presentation

import com.dcchua.gweather.R
import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.util.time.toLocalDate
import com.dcchua.gweather.core.util.time.toLocalTime
import com.dcchua.gweather.history.presentation.model.HistoryItem
import java.math.RoundingMode
import javax.inject.Inject

class HistoryItemFactory @Inject constructor() {

	fun toPresentation(weather: Weather.History): HistoryItem {
		return HistoryItem(
			iconId = getIconId(weather.weatherCondition),
			location = "${weather.area.city}, ${weather.area.country}",
			date = toLocalDate(weather.timestamp),
			temperature = weather.temperature.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toString(),
			sunrise = toLocalTime(weather.sunrise),
			sunset = toLocalTime(weather.sunset)
		)
	}

	private fun getIconId(
		condition: Weather.WeatherCondition,
	): Int {
		return when (condition) {
			Weather.WeatherCondition.Clear -> R.drawable.clear_sun
			Weather.WeatherCondition.Cloudy -> R.drawable.cloudy_sun
			Weather.WeatherCondition.Mist -> R.drawable.cloudy_sun
			Weather.WeatherCondition.Rain -> R.drawable.rain
			Weather.WeatherCondition.Snow -> R.drawable.snow
			Weather.WeatherCondition.ThunderStorm -> R.drawable.storm
		}
	}
}
