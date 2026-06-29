package com.dcchua.gweather.current.domain.model

import com.google.android.gms.common.api.ResolvableApiException

sealed class Location {
	data class FullData(
		val longitude: Double,
		val latitude: Double,
	) : Location()

	sealed class ErrorData : Location() {
		data object GPSUnsupported : ErrorData()
		data class RequiresResolution(val exception: ResolvableApiException) : ErrorData()
		data object PermissionRequired : ErrorData()
		data class Unknown(val message: String) : ErrorData()
	}

	data object NoData : Location()
}
