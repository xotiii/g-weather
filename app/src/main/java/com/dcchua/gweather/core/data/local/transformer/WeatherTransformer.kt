package com.dcchua.gweather.core.data.local.transformer

import com.dcchua.gweather.core.data.local.entity.WeatherEntity
import com.dcchua.gweather.core.domain.model.Weather
import javax.inject.Inject

class WeatherTransformer @Inject constructor() {

    fun toEntity(userId: Long, domain: Weather.Current): WeatherEntity {
        return WeatherEntity(
            userId = userId,
            temperature = domain.temperature,
            weatherCondition = when (domain.weatherCondition) {
                Weather.WeatherCondition.Clear -> WeatherEntity.WeatherCondition.CLEAR
                Weather.WeatherCondition.Cloudy -> WeatherEntity.WeatherCondition.CLOUDS
                Weather.WeatherCondition.Rain -> WeatherEntity.WeatherCondition.RAIN
                Weather.WeatherCondition.Snow -> WeatherEntity.WeatherCondition.SNOW
                Weather.WeatherCondition.ThunderStorm -> WeatherEntity.WeatherCondition.THUNDERSTORM
                Weather.WeatherCondition.Mist -> WeatherEntity.WeatherCondition.MIST
            },
            city = domain.area.city,
            country = domain.area.country,
            sunrise = domain.sunrise,
            sunset = domain.sunset,
        )
    }

    fun toDomain(entity: WeatherEntity): Weather.History {
        return Weather.History(
            weatherCondition = when (entity.weatherCondition) {
                WeatherEntity.WeatherCondition.CLEAR -> Weather.WeatherCondition.Clear
                WeatherEntity.WeatherCondition.CLOUDS -> Weather.WeatherCondition.Cloudy
                WeatherEntity.WeatherCondition.RAIN -> Weather.WeatherCondition.Rain
                WeatherEntity.WeatherCondition.SNOW -> Weather.WeatherCondition.Snow
                WeatherEntity.WeatherCondition.THUNDERSTORM -> Weather.WeatherCondition.ThunderStorm
                WeatherEntity.WeatherCondition.MIST -> Weather.WeatherCondition.Mist
            },
            temperature = entity.temperature,
            area = Weather.Area(
                city = entity.city,
                country = entity.country
            ),
            sunrise = entity.sunrise,
            sunset = entity.sunset,
            timestamp = entity.timestamp
        )
    }
}
