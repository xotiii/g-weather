package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.BuildConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetApiKeyTest {

    private val getApiKey = GetApiKey()

    @Test
    fun `invoke should return API key from BuildConfig`() {
        val expectedKey = BuildConfig.OPEN_WEATHER_API_KEY
        val actualKey = getApiKey()

        assertEquals(expectedKey, actualKey)
    }
}
