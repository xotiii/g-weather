package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.current.domain.CurrentWeatherRepository
import com.dcchua.gweather.current.domain.LocationRepository
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.current.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CurrentWeatherDataProvider @Inject constructor(
	private val currentWeatherRepository: CurrentWeatherRepository,
	private val locationRepository: LocationRepository,
	private val getApiKey: GetApiKey,
) {

	val dataStream: Flow<CurrentWeather> = combine(
		currentWeatherRepository.currentWeatherDataStream,
		locationRepository.locationDataStream,
	) { currentWeather, location ->
		when (location) {
			is Location.FullData -> {
				when (currentWeather) {
					CurrentWeather.NoData -> {
						currentWeatherRepository.fetchWeather(
							longitude = location.longitude,
							latitude = location.latitude,
							apiKey = getApiKey(),
						)
						currentWeather
					}
					else -> currentWeather
				}
			}
			Location.ErrorData.GPSUnsupported -> CurrentWeather.ErrorData.GPSUnsupported
			Location.ErrorData.PermissionRequired -> CurrentWeather.ErrorData.PermissionRequired
			is Location.ErrorData.RequiresResolution -> CurrentWeather.ErrorData.RequiresResolution(location.exception)
			is Location.ErrorData.Unknown -> CurrentWeather.ErrorData.LocationUnknown(location.message)
			Location.NoData -> CurrentWeather.NoData
		}
	}

	suspend fun getCurrentWeather() {
		locationRepository.fetchLocationData()
	}
}