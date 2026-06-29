package com.dcchua.gweather.current.data.remote.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentWeatherDto(
    @JsonProperty("coord") val coord: CoordDto,
    @JsonProperty("weather") val weather: List<WeatherDto>,
    @JsonProperty("base") val base: String,
    @JsonProperty("main") val main: MainDto,
    @JsonProperty("visibility") val visibility: Int,
    @JsonProperty("wind") val wind: WindDto,
    @JsonProperty("rain") val rain: RainDto? = null,
    @JsonProperty("clouds") val clouds: CloudsDto,
    @JsonProperty("dt") val dt: Long,
    @JsonProperty("sys") val sys: SysDto,
    @JsonProperty("timezone") val timezone: Int,
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("cod") val cod: Int
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CoordDto(
        @JsonProperty("lon") val lon: Double,
        @JsonProperty("lat") val lat: Double
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WeatherDto(
        @JsonProperty("id") val id: Int,
        @JsonProperty("main") val main: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("icon") val icon: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class MainDto(
        @JsonProperty("temp") val temp: Double,
        @JsonProperty("feels_like") val feelsLike: Double,
        @JsonProperty("temp_min") val tempMin: Double,
        @JsonProperty("temp_max") val tempMax: Double,
        @JsonProperty("pressure") val pressure: Int,
        @JsonProperty("humidity") val humidity: Int,
        @JsonProperty("sea_level") val seaLevel: Int? = null,
        @JsonProperty("grnd_level") val grndLevel: Int? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WindDto(
        @JsonProperty("speed") val speed: Double,
        @JsonProperty("deg") val deg: Int,
        @JsonProperty("gust") val gust: Double? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RainDto(
        @JsonProperty("1h") val oneHour: Double? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CloudsDto(
        @JsonProperty("all") val all: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SysDto(
        @JsonProperty("type") val type: Int? = null,
        @JsonProperty("id") val id: Int? = null,
        @JsonProperty("country") val country: String,
        @JsonProperty("sunrise") val sunrise: Long,
        @JsonProperty("sunset") val sunset: Long
    )
}
