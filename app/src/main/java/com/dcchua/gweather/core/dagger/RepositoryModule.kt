package com.dcchua.gweather.core.dagger

import com.dcchua.gweather.core.data.repository.UserRepositoryImpl
import com.dcchua.gweather.core.data.repository.WeatherRepositoryImpl
import com.dcchua.gweather.core.domain.repository.UserRepository
import com.dcchua.gweather.core.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
