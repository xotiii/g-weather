package com.dcchua.gweather.current.domain.usecase

import com.dcchua.gweather.BuildConfig
import javax.inject.Inject

class GetApiKey @Inject constructor() {

	operator fun invoke() = BuildConfig.OPEN_WEATHER_API_KEY

}