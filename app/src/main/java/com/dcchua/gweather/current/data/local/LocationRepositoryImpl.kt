package com.dcchua.gweather.current.data.local

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.dcchua.gweather.current.domain.LocationRepository
import com.dcchua.gweather.current.domain.model.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class LocationRepositoryImpl @Inject constructor(
	@param:ApplicationContext private val context: Context,
) : LocationRepository {

	private val locationManager =
		context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
	private val settingsClient = LocationServices.getSettingsClient(context)
	private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

	private val _locationDataStream = MutableStateFlow<Location>(Location.NoData)
	override val locationDataStream = _locationDataStream.asStateFlow()

	override suspend fun fetchLocationData() {
		_locationDataStream.value = Location.NoData
		delay(100.milliseconds)
		when {
			!isGpsHardwareAvailable() -> {
				_locationDataStream.value = Location.ErrorData.GPSUnsupported
				return
			}

			!hasRequiredPermissions() -> {
				_locationDataStream.value = Location.ErrorData.PermissionRequired
				return
			}

			!isGpsEnabled() -> {
				val settingsCheck = verifyGpsSettings()
				if (settingsCheck is Location.ErrorData.RequiresResolution ||
					settingsCheck is Location.ErrorData.Unknown
				) {
					_locationDataStream.value = settingsCheck
					return
				}
			}
		}

		try {
			_locationDataStream.value = when (val location = captureFreshCoordinates()) {
				null -> Location.ErrorData.Unknown("Failed to get coordinates")
				else -> {
					Location.FullData(
						longitude = location.longitude,
						latitude = location.latitude,
					)
				}
			}
		} catch (_: SecurityException) {
			_locationDataStream.value = Location.ErrorData.PermissionRequired
		} catch (e: Exception) {
			_locationDataStream.value =
				Location.ErrorData.Unknown(e.localizedMessage ?: "Failed to get coordinates")
		}
	}

	private fun hasRequiredPermissions(): Boolean {
		val fine =
			ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
		val coarse =
			ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
		return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
	}

	private fun isGpsHardwareAvailable(): Boolean {
		return context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
	}

	private fun isGpsEnabled(): Boolean {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
	}

	private suspend fun verifyGpsSettings(): Location? {
		var model: Location? = null
		val locationRequest =
			LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).build()
		val settingsRequest = LocationSettingsRequest.Builder()
			.addLocationRequest(locationRequest)
			.setAlwaysShow(true)
			.build()

		val task = settingsClient.checkLocationSettings(settingsRequest)
		try {
			task.await()
		} catch (exception: Exception) {
			model = if (exception is ResolvableApiException) {
				Location.ErrorData.RequiresResolution(exception = exception)
			} else {
				Location.ErrorData.Unknown(
					exception.localizedMessage ?: "Hardware configuration issue"
				)
			}
		}
		return model
	}

	@SuppressLint("MissingPermission")
	private suspend fun captureFreshCoordinates(): android.location.Location? {
		val cancellationTokenSource = CancellationTokenSource()
		return fusedLocationClient.getCurrentLocation(
			Priority.PRIORITY_HIGH_ACCURACY,
			cancellationTokenSource.token,
		).await()
	}
}
