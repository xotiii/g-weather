package com.dcchua.gweather.current.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dcchua.gweather.R
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status

class CurrentWeatherPreviewParameterProvider : PreviewParameterProvider<CurrentWeatherUiState> {

	override val values: Sequence<CurrentWeatherUiState>
		get() = sequenceOf(
		CurrentWeatherUiState.SuccessState(
			temperature = "30",
			weather = CurrentWeatherUiState.SuccessState.Weather(
				iconId = R.drawable.clear_sun,
				name = R.string.current_weather_weather_clear,
			),
			location = CurrentWeatherUiState.SuccessState.Location(
				city = "Taguig", country = "PH",
			),
			sunrise = "5:30 AM",
			sunset = "6:15 PM"
		),
		CurrentWeatherUiState.ErrorState.GPSUnsupported,
		CurrentWeatherUiState.ErrorState.LocationUnknown("Error message"),
		CurrentWeatherUiState.ErrorState.NetworkError,
		CurrentWeatherUiState.ErrorState.PermissionRequired,
		CurrentWeatherUiState.ErrorState.RequiresResolution(ResolvableApiException(Status.RESULT_SUCCESS)),
		CurrentWeatherUiState.Loading,
	)

}