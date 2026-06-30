package com.dcchua.gweather.current.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.dcchua.gweather.current.domain.model.Location
import com.dcchua.gweather.testutils.testFlowCollection
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.Task
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class LocationRepositoryImplTest {

	private lateinit var sut: LocationRepositoryImpl

	@RelaxedMockK
	private lateinit var context: Context

	private var locationManager = mockk<LocationManager>()

	private val settingsClient = mockk<SettingsClient>()

	private val fusedLocationClient = mockk<FusedLocationProviderClient>()

	@BeforeEach
	fun setup() {
		mockkStatic(LocationServices::class)
		mockkStatic(ContextCompat::class)
		Dispatchers.setMain(Dispatchers.Unconfined)
		every { context.getSystemService(Context.LOCATION_SERVICE) } returns locationManager
		every { LocationServices.getSettingsClient(context) } returns settingsClient
		every { LocationServices.getFusedLocationProviderClient(context) } returns fusedLocationClient
		sut = LocationRepositoryImpl(context)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
		unmockkStatic(LocationServices::class)
		unmockkStatic(ContextCompat::class)
	}

	@Test
	fun `check initial state`() = runTest {
		val flowCollection = testFlowCollection(sut.locationDataStream)

		advanceUntilIdle()

		assertEquals(Location.NoData, flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}

	@Test
	fun `verify if dataStream will return GPSUnsupported if user device does not support GPS`() = runTest {
		every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returns false
		every { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) } returns false
		val flowCollection = testFlowCollection(sut.locationDataStream)

		sut.fetchLocationData()
		advanceUntilIdle()

		coVerify(exactly = 0) { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) }
		assertEquals(Location.ErrorData.GPSUnsupported, flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}

	@Test
	fun `verify if dataStream will return PermissionRequired if user device has not allow location permission`() = runTest {
		every { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) } returns true
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) } returns PERMISSION_DENIED
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) } returns PERMISSION_DENIED

		val flowCollection = testFlowCollection(sut.locationDataStream)
		sut.fetchLocationData()
		advanceUntilIdle()

		coVerify(exactly = 0) { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) }
		assertEquals(Location.ErrorData.PermissionRequired, flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}

	@Test
	fun `verify if dataStream will return RequiresResolution if user device gps is turned off`() = runTest {
		val resolvableException = mockk<ResolvableApiException>(relaxed = true)
		val task = mockk<Task<LocationSettingsResponse>>()
		every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returns false
		every { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) } returns true
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) } returns PERMISSION_GRANTED
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) } returns PERMISSION_GRANTED
		every { settingsClient.checkLocationSettings(any()) } returns task
		every { task.isComplete } returns true
		every { task.exception } returns resolvableException
		every { task.isCanceled } returns false
		val flowCollection = testFlowCollection(sut.locationDataStream)

		sut.fetchLocationData()
		advanceUntilIdle()

		coVerify(exactly = 0) { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) }
		assertEquals(Location.ErrorData.RequiresResolution(resolvableException), flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}

	@Test
	fun `verify if dataStream will return FullData if user device gps detects location`() = runTest {
		val settingsTask = mockk<Task<LocationSettingsResponse>>()
		val locationTask = mockk<Task<android.location.Location>>()
		val long = 123.0
		val lat = 18.0
		every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returns true
		every { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) } returns true
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) } returns PERMISSION_GRANTED
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) } returns PERMISSION_GRANTED
		every { settingsClient.checkLocationSettings(any()) } returns settingsTask
		every { settingsTask.isComplete } returns true
		every { settingsTask.result } returns mockk()
		every { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) } returns locationTask
		every { locationTask.isComplete } returns true
		every { locationTask.exception } returns null
		every { locationTask.isCanceled } returns false
		every { locationTask.result } returns mockk<android.location.Location> {
			every { longitude } returns long
			every { latitude } returns lat
		}
		val flowCollection = testFlowCollection(sut.locationDataStream)

		sut.fetchLocationData()
		advanceUntilIdle()

		coVerify(exactly = 1) { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) }
		assertEquals(Location.FullData(long, lat), flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}

	@Test
	fun `verify if dataStream will return Unknown Error if user device gps returns null`() = runTest {
		val settingsTask = mockk<Task<LocationSettingsResponse>>()
		val locationTask = mockk<Task<android.location.Location>>()
		every { locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) } returns true
		every { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) } returns true
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) } returns PERMISSION_GRANTED
		every { ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) } returns PERMISSION_GRANTED
		every { settingsClient.checkLocationSettings(any()) } returns settingsTask
		every { settingsTask.isComplete } returns true
		every { settingsTask.result } returns mockk()
		every { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) } returns locationTask
		every { locationTask.isComplete } returns true
		every { locationTask.exception } returns null
		every { locationTask.isCanceled } returns false
		every { locationTask.result } returns null
		val flowCollection = testFlowCollection(sut.locationDataStream)

		sut.fetchLocationData()
		advanceUntilIdle()

		coVerify(exactly = 1) { fusedLocationClient.getCurrentLocation(any<Int>(), any<CancellationToken>()) }
		assertEquals(Location.ErrorData.Unknown("Failed to get coordinates"), flowCollection.getLatestValue())
		flowCollection.finishCollection()
	}
}