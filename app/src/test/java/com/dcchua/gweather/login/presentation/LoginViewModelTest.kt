package com.dcchua.gweather.login.presentation

import com.dcchua.gweather.login.domain.usecase.LoginUser
import com.dcchua.gweather.login.presentation.state.LoginUiState
import com.dcchua.gweather.testutils.testFlowCollection
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
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
class LoginViewModelTest {

    private lateinit var sut: LoginViewModel

    @RelaxedMockK
    private lateinit var loginUser: LoginUser

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        sut = LoginViewModel(loginUser)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Initial`() {
        assertEquals(LoginUiState.Initial, sut.uiState.value)
    }

    @Test
    fun `viewModel should emit Success when login is successful`() = runTest {
        val username = "testuser"
        val password = "password"
        coEvery { loginUser(username, password) } returns true

        val flowCollection = testFlowCollection(sut.uiState)

        sut.login(username, password)
        advanceUntilIdle()

        assertEquals(LoginUiState.Success, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `login should should emit Error when login fails`() = runTest {
        val username = "testuser"
        val password = "password"
        coEvery { loginUser(username, password) } returns false

        val flowCollection = testFlowCollection(sut.uiState)

        sut.login(username, password)
        advanceUntilIdle()

        assertEquals(LoginUiState.Error, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `login should should emit Error when exception occurs`() = runTest {
        val username = "testuser"
        val password = "password"
        coEvery { loginUser(username, password) } throws Exception("Network error")

        val flowCollection = testFlowCollection(sut.uiState)

        sut.login(username, password)
        advanceUntilIdle()

        assertEquals(LoginUiState.Error, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `resetState should should emit uiState back to Initial`() = runTest {
        coEvery { loginUser(any(), any()) } returns false
        sut.login("user", "pass")
        advanceUntilIdle()
        assertEquals(LoginUiState.Error, sut.uiState.value)

        sut.resetState()
        assertEquals(LoginUiState.Initial, sut.uiState.value)
    }
}
