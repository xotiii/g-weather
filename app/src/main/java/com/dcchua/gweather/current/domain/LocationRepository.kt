package com.dcchua.gweather.current.domain

import com.dcchua.gweather.current.domain.model.Location
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

	val locationDataStream : StateFlow<Location>

	suspend fun fetchLocationData()

}