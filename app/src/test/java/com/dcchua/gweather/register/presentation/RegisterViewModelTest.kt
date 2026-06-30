package com.dcchua.gweather.register.presentation

import android.database.sqlite.SQLiteConstraintException
import com.dcchua.gweather.register.domain.usecase.RegisterUser
import com.dcchua.gweather.register.presentation.state.RegisterUiState
import com.dcchua.gweather.testutils.testFlowCollection
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RegisterViewModelTest {

    private lateinit var sut: RegisterViewModel

    @RelaxedMockK
    private lateinit var registerUser: RegisterUser

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        sut = RegisterViewModel(registerUser)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Initial`() {
        assertEquals(RegisterUiState.Initial, sut.uiState.value)
    }

    @Test
    fun `viewModel should emit Success when registration is successful`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        coEvery { registerUser(username, password, firstName) } returns 1L

        val flowCollection = testFlowCollection(sut.uiState)

        sut.register(username, password, firstName)
        advanceUntilIdle()

        assertEquals(RegisterUiState.Success, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `viewModel should emit Error UsernameExist when SQLiteConstraintException occurs`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        coEvery { registerUser(username, password, firstName) } throws mockk<SQLiteConstraintException>()

        val flowCollection = testFlowCollection(sut.uiState)

        sut.register(username, password, firstName)
        advanceUntilIdle()

        assertEquals(RegisterUiState.Error.UsernameExist, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `viewModel should emit Error Unknown when other exceptions occur`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        coEvery { registerUser(username, password, firstName) } throws Exception("Unknown error")

        val flowCollection = testFlowCollection(sut.uiState)

        sut.register(username, password, firstName)
        advanceUntilIdle()

        assertEquals(RegisterUiState.Error.Unknown, flowCollection.getLatestValue())
        flowCollection.finishCollection()
    }

    @Test
    fun `resetState should emit uiState back to Initial`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        coEvery { registerUser(username, password, firstName) } throws Exception("error")
        
        sut.register(username, password, firstName) // Trigger error state
        assertTrue(sut.uiState.value is RegisterUiState.Error)

        sut.resetState()
        assertEquals(RegisterUiState.Initial, sut.uiState.value)
    }
}
