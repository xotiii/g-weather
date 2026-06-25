package com.dcchua.gweather.current.presentation

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcchua.gweather.current.presentation.state.CurrentWeatherUiState

@Composable
fun CurrentWeatherScreen(
	viewModel: CurrentWeatherViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiStream.collectAsStateWithLifecycle(initialValue = CurrentWeatherUiState.Loading)

	Scaffold { innerPaddings ->
		Box(
			modifier = Modifier.consumeWindowInsets(innerPaddings)
		) {
			CurrentWeatherContent(
				state = uiState,
				locationPermissionLauncher = getLocationPermissionLauncher { viewModel.loadLocation() },
				gpsResolutionLauncher = getGPSResolutionLauncher { viewModel.loadLocation() },
			)
		}
	}
}

@Composable
fun CurrentWeatherContent(
	state: CurrentWeatherUiState,
	locationPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
	gpsResolutionLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		when (state) {
			is CurrentWeatherUiState.ErrorState.RequiresResolution -> Button(
				onClick = {
					val request =
						IntentSenderRequest.Builder(state.exception.resolution.intentSender).build()
					gpsResolutionLauncher.launch(request)
				}
			) {
				Text("Turn On GPS")
			}

			CurrentWeatherUiState.ErrorState.GPSUnsupported -> Text("Your device doesn't support GPS.")
			CurrentWeatherUiState.ErrorState.PermissionRequired -> Button(
				onClick = {
					locationPermissionLauncher.launch(
						arrayOf(
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION
						)
					)
				}
			) {
				Text("Request Location Permission")
			}

			is CurrentWeatherUiState.ErrorState.Unknown -> Text(state.message)
			CurrentWeatherUiState.Loading -> Text("Fetching Location...")
			is CurrentWeatherUiState.SuccessState.DayTime -> {
				Text("Day Time")
				Text("${state.temperature} ${state.weather}")
			}

			is CurrentWeatherUiState.SuccessState.NightTime -> {
				Text("Night Time")
				Text("${state.temperature} ${state.weather}")
			}
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
	if (result.resultCode == android.app.Activity.RESULT_OK) {
		onSuccess()
	}
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherContentPreview() {
	CurrentWeatherContent(
		state = CurrentWeatherUiState.ErrorState.GPSUnsupported,
		locationPermissionLauncher = getLocationPermissionLauncher { },
		gpsResolutionLauncher = getGPSResolutionLauncher { },
	)
}