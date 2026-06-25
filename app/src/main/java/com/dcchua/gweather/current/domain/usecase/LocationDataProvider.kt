package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.current.domain.LocationRepository
import javax.inject.Inject

class LocationDataProvider @Inject constructor(
	private val repository: LocationRepository,
) {

	fun getDataStream() = repository.locationDataStream

	suspend fun getLocationData() = repository.fetchLocationData()

}