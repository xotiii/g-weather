package com.dcchua.gweather.current.di

import com.dcchua.gweather.current.data.LocationRepositoryImpl
import com.dcchua.gweather.current.domain.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrentWeatherModule {

	@Binds
	abstract fun provideLocationRepository(
		repositoryImpl: LocationRepositoryImpl,
	) : LocationRepository
}