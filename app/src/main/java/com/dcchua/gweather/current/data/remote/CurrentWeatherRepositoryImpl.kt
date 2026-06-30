package com.dcchua.gweather.current.data.remote

import com.dcchua.gweather.core.domain.usecase.GetUser
import com.dcchua.gweather.current.data.remote.api.WeatherApi
import com.dcchua.gweather.current.data.remote.transformer.CurrentWeatherTransformer
import com.dcchua.gweather.current.domain.CurrentWeatherRepository
import com.dcchua.gweather.current.domain.model.CurrentWeather
import com.dcchua.gweather.core.util.coroutines.coRunCatching
import com.dcchua.gweather.current.domain.usecase.SaveWeatherHistory
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Retrofit
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(
	retrofit: Lazy<Retrofit>,
	private val transformer: CurrentWeatherTransformer,
	private val saveWeatherHistory: SaveWeatherHistory,
	private val getUser: GetUser,
) : CurrentWeatherRepository {

	private val api: WeatherApi by lazy { retrofit.get().create(WeatherApi::class.java) }
	private val _currentWeatherDataStream = MutableStateFlow<CurrentWeather>(CurrentWeather.NoData)
	override val currentWeatherDataStream: StateFlow<CurrentWeather> = _currentWeatherDataStream.asStateFlow()

	override suspend fun fetchWeather(
		longitude: Double,
		latitude: Double,
		apiKey: String
	) {
		coRunCatching {
			if (_currentWeatherDataStream.value is CurrentWeather.ErrorData) {
				_currentWeatherDataStream.value = CurrentWeather.NoData
			}
			transformer.toDomain(api.getCurrentWeather(
				longitude = longitude,
				latitude = latitude,
				apiKey = apiKey,
			))
		}.onSuccess {
			getUser()?.let { user ->
				if (it is CurrentWeather.FullData) saveWeatherHistory(userId = user.id, state = it)
			}
			_currentWeatherDataStream.value = it
		}.onFailure {
			_currentWeatherDataStream.value = CurrentWeather.ErrorData.NetworkError
		}
	}
}