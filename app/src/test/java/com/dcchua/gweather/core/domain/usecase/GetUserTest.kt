package com.dcchua.gweather.core.domain.usecase

import com.dcchua.gweather.core.data.local.preferences.SharedPreferencesUtil
import com.dcchua.gweather.core.domain.model.User
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class GetUserTest {

    private lateinit var sut: GetUser

    @RelaxedMockK
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @BeforeEach
    fun setup() {
        sut = GetUser(sharedPreferencesUtil)
    }

    @Test
    fun `invoke should return user from sharedPreferencesUtil`() {
        val user = User(id = 1L, firstName = "John")
        every { sharedPreferencesUtil.getUser() } returns user

        val result = sut()

        assertEquals(user, result)
    }
}
