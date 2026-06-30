package com.dcchua.gweather.register.domain.usecase

import com.dcchua.gweather.core.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class RegisterUserTest {

    private lateinit var sut: RegisterUser

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        sut = RegisterUser(userRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call registerUser from repository`() = runTest {
        val username = "testuser"
        val password = "password123"
        val firstName = "John"
        coEvery { userRepository.registerUser(username, password, firstName) } returns 1L

        sut(username, password, firstName)

        coVerify { userRepository.registerUser(username, password, firstName) }
    }
}
