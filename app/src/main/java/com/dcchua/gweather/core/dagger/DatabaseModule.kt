package com.dcchua.gweather.core.dagger

import android.content.Context
import androidx.room.Room
import com.dcchua.gweather.core.data.local.dao.UserDao
import com.dcchua.gweather.core.data.local.dao.WeatherDao
import com.dcchua.gweather.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "g-weather-database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideWeatherDao(database: AppDatabase): WeatherDao = database.weatherDao()
}
