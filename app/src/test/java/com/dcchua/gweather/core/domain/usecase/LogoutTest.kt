package com.dcchua.gweather.core.domain.usecase

import com.dcchua.gweather.core.data.local.preferences.SharedPreferencesUtil
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class LogoutTest {

    private lateinit var sut: Logout

    @RelaxedMockK
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @BeforeEach
    fun setup() {
        sut = Logout(sharedPreferencesUtil)
    }

    @Test
    fun `invoke should call clearUser from sharedPreferencesUtil`() {
        sut()

        verify { sharedPreferencesUtil.clearUser() }
    }
}
