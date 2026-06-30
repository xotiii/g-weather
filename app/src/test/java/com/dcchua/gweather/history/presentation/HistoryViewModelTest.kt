package com.dcchua.gweather.history.presentation

import com.dcchua.gweather.core.domain.model.User
import com.dcchua.gweather.core.domain.model.Weather
import com.dcchua.gweather.core.domain.usecase.GetUser
import com.dcchua.gweather.history.domain.usecase.GetWeatherHistory
import com.dcchua.gweather.history.presentation.model.HistoryItem
import com.dcchua.gweather.testutils.testFlowCollection
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class HistoryViewModelTest {

    private lateinit var sut: HistoryViewModel

    @RelaxedMockK
    private lateinit var getWeatherHistory: GetWeatherHistory

    @RelaxedMockK
    private lateinit var getUser: GetUser

    @RelaxedMockK
    private lateinit var factory: HistoryItemFactory

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `historyList should emit mapped history items`() = runTest {
        val user = User(id = 1L, firstName = "John")
        val weatherHistory = listOf(mockk<Weather.History>())
        val historyItem = mockk<HistoryItem>()

        every { getUser() } returns user
        every { getWeatherHistory(1L) } returns flowOf(weatherHistory)
        every { factory.toPresentation(any()) } returns historyItem

        sut = HistoryViewModel(getWeatherHistory, getUser, factory)
        val flowCollection = testFlowCollection(sut.historyList)
        advanceUntilIdle()

        assertEquals(listOf(historyItem), flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `historyList should emit empty list when user is null`() = runTest {
        every { getUser() } returns null
        every { getWeatherHistory(-1L) } returns flowOf(emptyList())

        sut = HistoryViewModel(getWeatherHistory, getUser, factory)
        val flowCollection = testFlowCollection(sut.historyList)
        advanceUntilIdle()

        assertEquals(emptyList<HistoryItem>(), flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }
}
