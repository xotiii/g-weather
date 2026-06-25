package com.dcchua.gweather.current.domain.model

import com.google.android.gms.common.api.ResolvableApiException

sealed interface Location {
	data class FullData(
		val longitude: Double,
		val latitude: Double,
	) : Location

	sealed interface ErrorData : Location {
		data object GPSUnsupported : ErrorData
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorData
		data object PermissionRequired : ErrorData
		data class Unknown(val message: String) : ErrorData
	}

	data object NoData : Location
}
