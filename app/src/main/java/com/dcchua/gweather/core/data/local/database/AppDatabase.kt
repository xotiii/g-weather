package com.dcchua.gweather.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dcchua.gweather.core.data.local.dao.UserDao
import com.dcchua.gweather.core.data.local.dao.WeatherDao
import com.dcchua.gweather.core.data.local.entity.UserEntity
import com.dcchua.gweather.core.data.local.entity.WeatherEntity

@Database(
	entities = [
		UserEntity::class,
		WeatherEntity::class,
	],
	version = 1,
	exportSchema = false,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun weatherDao(): WeatherDao
}