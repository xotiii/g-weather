package com.dcchua.gweather.current.data.remote.api

import com.dcchua.gweather.current.data.remote.api.dto.CurrentWeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

	@GET("/data/2.5/weather")
	suspend fun getCurrentWeather(
		@Query("units") units: String = "metric",
		@Query("lon") longitude: Double,
		@Query("lat") latitude: Double,
		@Query("appid") apiKey: String
	) : CurrentWeatherDto

}