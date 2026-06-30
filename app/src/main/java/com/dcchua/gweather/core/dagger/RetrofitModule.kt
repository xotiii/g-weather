package com.dcchua.gweather.core.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

	@Provides
	fun provideRetrofit(): Retrofit {
		return Retrofit.Builder()
			.baseUrl("https://api.openweathermap.org/")
			.client(
				OkHttpClient.Builder()
					.addInterceptor(HttpLoggingInterceptor().apply {
						level = HttpLoggingInterceptor.Level.BODY
					}).build()
			).addConverterFactory(JacksonConverterFactory.create())
			.build()
	}
}