package com.dcchua.gweather.core.domain.usecase

import com.dcchua.gweather.core.data.local.preferences.SharedPreferencesUtil
import javax.inject.Inject

class GetUser @Inject constructor(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) {
    operator fun invoke() = sharedPreferencesUtil.getUser()
}
