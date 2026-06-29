package com.dcchua.gweather.current.presentation

import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.current.domain.usecase.CurrentWeatherDataProvider
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import com.dcchua.gweather.testutils.testFlowCollection
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CurrentWeatherViewModelTest {

    private lateinit var sut: CurrentWeatherViewModel

    @RelaxedMockK
    private lateinit var currentWeatherDataProvider: CurrentWeatherDataProvider

    @RelaxedMockK
    private lateinit var factory: CurrentWeatherUiStateFactory

    private val dataStream = MutableStateFlow<CurrentWeather>(CurrentWeather.NoData)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockkStatic(LocalDateTime::class)
        every { currentWeatherDataProvider.dataStream } returns dataStream
        sut = CurrentWeatherViewModel(
            currentWeatherDataProvider,
            factory,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `check initial state`() = runTest {
        coVerify(exactly = 1) { currentWeatherDataProvider.getCurrentWeather() }
    }

    @Test
    fun `uiStream should emit mapped state from factory for day time`() = runTest {
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 1, 1, 10, 0)
        val viewModel = CurrentWeatherViewModel(currentWeatherDataProvider, factory)

        val weather = mockk<CurrentWeather>()
        val expectedState = mockk<CurrentWeatherUiState>()
        
        every { factory.toUiState(weather, true) } returns expectedState
        
        val flowCollection = testFlowCollection(viewModel.uiStream)
        
        dataStream.value = weather
        advanceUntilIdle()

        assertEquals(expectedState, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `uiStream should emit mapped state from factory for night time`() = runTest {
        every { LocalDateTime.now() } returns LocalDateTime.of(2024, 1, 1, 20, 0)
        val viewModel = CurrentWeatherViewModel(currentWeatherDataProvider, factory)

        val weather = mockk<CurrentWeather>()
        val expectedState = mockk<CurrentWeatherUiState>()
        
        every { factory.toUiState(weather, false) } returns expectedState
        
        val flowCollection = testFlowCollection(viewModel.uiStream)
        
        dataStream.value = weather
        advanceUntilIdle()

        assertEquals(expectedState, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `isDayTime should be true when hour is between 6 and 17`() = runTest {
        val dayTime = LocalDateTime.of(2024, 1, 1, 10, 0)
        every { LocalDateTime.now() } returns dayTime

        val viewModel = CurrentWeatherViewModel(currentWeatherDataProvider, factory)

        assertEquals(true, viewModel.isDayTime.value)
    }

    @Test
    fun `isDayTime should be false when hour is outside 6 to 17`() = runTest {
        val nightTime = LocalDateTime.of(2024, 1, 1, 20, 0)
        every { LocalDateTime.now() } returns nightTime

        val viewModel = CurrentWeatherViewModel(currentWeatherDataProvider, factory)

        assertEquals(false, viewModel.isDayTime.value)
    }
}
