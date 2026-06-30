package com.dcchua.gweather.core.dagger

import com.dcchua.gweather.current.data.remote.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

	@Provides
	internal fun weatherApi(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)
}