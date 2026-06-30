package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.current.domain.CurrentWeatherRepository
import com.dcchua.gweather.current.domain.LocationRepository
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.current.domain.model.Location
import com.dcchua.gweather.testutils.testFlowCollection
import com.google.android.gms.common.api.ResolvableApiException
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CurrentWeatherDataProviderTest {

    private lateinit var sut: CurrentWeatherDataProvider

    @RelaxedMockK
    private lateinit var currentWeatherRepository: CurrentWeatherRepository

    @RelaxedMockK
    private lateinit var locationRepository: LocationRepository

    @RelaxedMockK
    private lateinit var getApiKey: GetApiKey

    private val currentWeatherDataStream = MutableStateFlow<CurrentWeather>(CurrentWeather.NoData)
    private val locationDataStream = MutableStateFlow<Location>(Location.NoData)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        every { currentWeatherRepository.currentWeatherDataStream } returns currentWeatherDataStream
        every { locationRepository.locationDataStream } returns locationDataStream
        every { getApiKey() } returns "test_api_key"

        sut = CurrentWeatherDataProvider(
            currentWeatherRepository,
            locationRepository,
            getApiKey
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentWeather should call fetchLocationData`() = runTest {
        sut.getCurrentWeather()
        coVerify(exactly = 1) { locationRepository.fetchLocationData() }
    }

    @Test
    fun `dataStream should emit NoData when location is NoData`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        
        locationDataStream.value = Location.NoData
        advanceUntilIdle()

        assertEquals(CurrentWeather.NoData, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should emit GPSUnsupported when location is GPSUnsupported`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        
        locationDataStream.value = Location.ErrorData.GPSUnsupported
        advanceUntilIdle()

        assertEquals(CurrentWeather.ErrorData.GPSUnsupported, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should emit PermissionRequired when location is PermissionRequired`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        
        locationDataStream.value = Location.ErrorData.PermissionRequired
        advanceUntilIdle()

        assertEquals(CurrentWeather.ErrorData.PermissionRequired, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should emit RequiresResolution when location is RequiresResolution`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        val exception = mockk<ResolvableApiException>()
        
        locationDataStream.value = Location.ErrorData.RequiresResolution(exception)
        advanceUntilIdle()

        val latestValue = flowCollection.getLatestValue()
        assertTrue(latestValue is CurrentWeather.ErrorData.RequiresResolution)
        assertEquals(exception, (latestValue as CurrentWeather.ErrorData.RequiresResolution).exception)
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should emit LocationUnknown when location is Unknown`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        
        locationDataStream.value = Location.ErrorData.Unknown("Error")
        advanceUntilIdle()

        assertEquals(CurrentWeather.ErrorData.LocationUnknown("Error"), flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should call fetchWeather when location is FullData and currentWeather is NoData`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        val longitude = 10.0
        val latitude = 20.0
        
        locationDataStream.value = Location.FullData(longitude, latitude)
        advanceUntilIdle()

        coVerify(exactly = 1) { 
            currentWeatherRepository.fetchWeather(longitude, latitude, "test_api_key") 
        }
        assertEquals(CurrentWeather.NoData, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `dataStream should emit FullData when location is FullData and currentWeather is FullData`() = runTest {
        val flowCollection = testFlowCollection(sut.dataStream)
        val fullData = mockk<CurrentWeather.FullData>()
        
        currentWeatherDataStream.value = fullData
        locationDataStream.value = Location.FullData(10.0, 20.0)
        advanceUntilIdle()

        assertEquals(fullData, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }
}
