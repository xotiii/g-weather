package com.dcchua.gweather.current.presentation

import android.Manifest
import android.app.Activity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcchua.gweather.R
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState
import com.dcchua.gweather.presentation.theme.GWeatherTheme
import com.dcchua.gweather.presentation.theme.Typography

@Composable
fun CurrentWeatherScreen(
	viewModel: CurrentWeatherViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiStream.collectAsStateWithLifecycle(initialValue = CurrentWeatherUiState.Loading)
	val isDayTime by viewModel.isDayTime.collectAsStateWithLifecycle(initialValue = true)

	GWeatherTheme(
		darkTheme = !isDayTime,
	) {
		Scaffold { innerPaddings ->
			Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
				CurrentWeatherContent(
					state = uiState,
					isDayTime = isDayTime,
					locationPermissionLauncher = getLocationPermissionLauncher { viewModel.getCurrentWeather() },
					gpsResolutionLauncher = getGPSResolutionLauncher { viewModel.getCurrentWeather() },
					getCurrentWeather = viewModel::getCurrentWeather,
				)
			}
		}
	}
}

@Composable
fun CurrentWeatherContent(
	state: CurrentWeatherUiState,
	isDayTime: Boolean,
	getCurrentWeather: () -> Unit,
	locationPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
	gpsResolutionLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.paint(
				painter = painterResource(
					id = if (isDayTime) {
						R.drawable.day_background
					} else {
						R.drawable.night_background
					}
				),
				contentScale = ContentScale.FillHeight,
			),
	) {
		when (state) {
			is CurrentWeatherUiState.ErrorState.RequiresResolution ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) {
					Button(
						onClick = {
							val request =
								IntentSenderRequest.Builder(state.exception.resolution.intentSender)
									.build()
							gpsResolutionLauncher.launch(request)
						}
					) {
						Text("Turn On GPS")
					}
				}

			CurrentWeatherUiState.ErrorState.GPSUnsupported ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) { Text(stringResource(R.string.current_weather_gps_unsupported)) }

			CurrentWeatherUiState.ErrorState.PermissionRequired ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) {
					Button(
						onClick = {
							locationPermissionLauncher.launch(
								arrayOf(
									Manifest.permission.ACCESS_FINE_LOCATION,
									Manifest.permission.ACCESS_COARSE_LOCATION
								)
							)
						}
					) {
						Text(stringResource(R.string.current_weather_request_permission))
					}
				}

			is CurrentWeatherUiState.ErrorState.LocationUnknown ->
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Text(state.message)
					Spacer(modifier = Modifier.size(8.dp))
					Button(onClick = getCurrentWeather) {
						Text(stringResource(R.string.current_weather_try_again))
					}
				}

			CurrentWeatherUiState.Loading ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) { CircularProgressIndicator() }

			is CurrentWeatherUiState.SuccessState -> SuccessContent(state = state)

			is CurrentWeatherUiState.ErrorState.NetworkError ->
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Text(stringResource(R.string.current_weather_network_error))
					Spacer(modifier = Modifier.size(8.dp))
					Button(onClick = getCurrentWeather) {
						Text(stringResource(R.string.current_weather_try_again))
					}
				}
		}
	}
}

@Composable
private fun SuccessContent(
	state: CurrentWeatherUiState.SuccessState,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			modifier = Modifier.size(200.dp),
			painter = painterResource(state.weather.iconId),
			contentDescription = null,
		)
		Spacer(modifier = Modifier.size(16.dp))
		Text(
			text = stringResource(
				R.string.current_weather_temperature_celsius,
				state.temperature
			),
			style = Typography.displayMedium,
		)
		Spacer(modifier = Modifier.size(8.dp))
		Text(
			text = "${state.location.city}, ${state.location.country}",
			style = Typography.headlineSmall,
		)
		Spacer(modifier = Modifier.size(24.dp))
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			Image(
				modifier = Modifier.size(32.dp),
				painter = painterResource(R.drawable.ic_sunrise),
				contentDescription = null,
			)
			Text(
				text = state.sunrise,
				style = Typography.titleLarge,
			)
		}
		Spacer(modifier = Modifier.size(8.dp))
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			Image(
				modifier = Modifier.size(32.dp),
				painter = painterResource(R.drawable.ic_sunset),
				contentDescription = null,
			)
			Text(
				text = state.sunset,
				style = Typography.titleLarge,
			)
		}
	}
}

@Composable
private fun getLocationPermissionLauncher(
	result: (Boolean) -> Unit,
) = rememberLauncherForActivityResult(
	contract = ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
	val isFineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
	val isCoarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
	result(isFineGranted || isCoarseGranted)
}

@Composable
private fun getGPSResolutionLauncher(
	onSuccess: () -> Unit,
) = rememberLauncherForActivityResult(
	contract = ActivityResultContracts.StartIntentSenderForResult()
) { result ->
	if (result.resultCode == Activity.RESULT_OK) {
		onSuccess()
	}
}


@PreviewLightDark
@Composable
fun CurrentWeatherContentPreview(
	@PreviewParameter(CurrentWeatherPreviewParameterProvider::class) uiState: CurrentWeatherUiState,
) {
	GWeatherTheme {
		CurrentWeatherContent(
			state = uiState,
			isDayTime = !isSystemInDarkTheme(),
			locationPermissionLauncher = getLocationPermissionLauncher { },
			gpsResolutionLauncher = getGPSResolutionLauncher { },
			getCurrentWeather = {},
		)
	}
}