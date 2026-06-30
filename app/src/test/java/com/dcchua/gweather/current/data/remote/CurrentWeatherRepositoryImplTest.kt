package com.dcchua.gweather.current.data.remote

import com.dcchua.gweather.current.data.remote.api.WeatherApi
import com.dcchua.gweather.current.data.remote.api.dto.CurrentWeatherDto
import com.dcchua.gweather.current.data.remote.transformer.CurrentWeatherTransformer
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.testutils.testFlowCollection
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
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
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CurrentWeatherRepositoryImplTest {

	private lateinit var sut: CurrentWeatherRepositoryImpl

	@RelaxedMockK
	private lateinit var api: WeatherApi

	@RelaxedMockK
	private lateinit var retrofit: Retrofit

	@RelaxedMockK
	private lateinit var transformer: CurrentWeatherTransformer

	@BeforeEach
	fun setup() {
		Dispatchers.setMain(Dispatchers.Unconfined)
		val retrofitProvider = Lazy { retrofit }
		every { retrofit.create(any<Class<WeatherApi>>()) } returns api
		sut = CurrentWeatherRepositoryImpl(
			retrofit = retrofitProvider,
			transformer = transformer,
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `check initial state`() = runTest {
		val flowCollector = testFlowCollection(sut.currentWeatherDataStream)

		advanceUntilIdle()

		assertEquals(CurrentWeather.NoData, flowCollector.getLatestValue())
		flowCollector.finishCollection()
	}

	@Test
	fun `verify repository returns full data when API returns proper response`() = runTest {
		val apiResponse = mockk<CurrentWeatherDto>()
		val fullData = mockk<CurrentWeather.FullData>()
		coEvery {
			api.getCurrentWeather(
				longitude = any(),
				latitude = any(),
				apiKey = any()
			)
		} returns apiResponse
		every { transformer.toDomain(apiResponse) } returns fullData
		val flowCollector = testFlowCollection(sut.currentWeatherDataStream)

		sut.fetchWeather(longitude = 123.0, latitude = 123.0, apiKey = "123")
		advanceUntilIdle()

		coVerify { api.getCurrentWeather(longitude = 123.0, latitude = 123.0, apiKey = "123") }
		verify { transformer.toDomain(apiResponse) }
		assertEquals(fullData, flowCollector.getLatestValue())
		flowCollector.finishCollection()
	}

	@Test
	fun `verify repository returns network error data when API throws exception`() = runTest {
		val apiResponse = mockk<CurrentWeatherDto>()
		coEvery {
			api.getCurrentWeather(
				longitude = any(),
				latitude = any(),
				apiKey = any()
			)
		} throws mockk()
		every { transformer.toDomain(apiResponse) } returns mockk()
		val flowCollector = testFlowCollection(sut.currentWeatherDataStream)

		sut.fetchWeather(longitude = 123.0, latitude = 123.0, apiKey = "123")
		advanceUntilIdle()

		coVerify { api.getCurrentWeather(longitude = 123.0, latitude = 123.0, apiKey = "123") }
		verify(exactly = 0) { transformer.toDomain(apiResponse) }
		assertEquals(CurrentWeather.ErrorData.NetworkError, flowCollector.getLatestValue())
		flowCollector.finishCollection()
	}

	@Test
	fun `verify repository returns proper data when API throws exception then user tries again and succeed`() =
		runTest {
			val apiResponse = mockk<CurrentWeatherDto>()
			val fullData = mockk<CurrentWeather.FullData>()
			coEvery {
				api.getCurrentWeather(
					longitude = any(),
					latitude = any(),
					apiKey = any()
				)
			} throws mockk() andThen apiResponse
			every { transformer.toDomain(apiResponse) } returns fullData
			val flowCollector = testFlowCollection(sut.currentWeatherDataStream)

			sut.fetchWeather(longitude = 123.0, latitude = 123.0, apiKey = "123")
			advanceUntilIdle()
			sut.fetchWeather(longitude = 123.0, latitude = 123.0, apiKey = "123")
			advanceUntilIdle()

			coVerify { api.getCurrentWeather(longitude = 123.0, latitude = 123.0, apiKey = "123") }
			verify(exactly = 1) { transformer.toDomain(apiResponse) }
			assertEquals(
				listOf(
					CurrentWeather.NoData,
					CurrentWeather.ErrorData.NetworkError,
					CurrentWeather.NoData,
					fullData,
				),
				flowCollector.getValues()
			)
			flowCollector.finishCollection()
		}

}