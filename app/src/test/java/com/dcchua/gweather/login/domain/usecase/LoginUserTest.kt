package com.dcchua.gweather.login.domain.usecase

import com.dcchua.gweather.core.data.local.preferences.SharedPreferencesUtil
import com.dcchua.gweather.core.domain.model.User
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class LoginUserTest {

    private lateinit var sut: LoginUser

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    @RelaxedMockK
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        sut = LoginUser(userRepository, sharedPreferencesUtil)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return true and save user when repository returns user`() = runTest {
        val username = "testuser"
        val password = "password"
        val user = User(id = 1L, firstName = "John")
        coEvery { userRepository.loginUser(username, password) } returns user

        val result = sut(username, password)

        assertTrue(result)
        coVerify { sharedPreferencesUtil.saveUser(user) }
    }

    @Test
    fun `invoke should return false when repository returns null`() = runTest {
        val username = "testuser"
        val password = "password"
        coEvery { userRepository.loginUser(username, password) } returns null

        val result = sut(username, password)

        assertFalse(result)
        coVerify(exactly = 0) { sharedPreferencesUtil.saveUser(any()) }
    }
}
