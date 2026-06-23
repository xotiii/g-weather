package com.dcchua.gweather.current.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CurrentWeatherScreen() {
	Scaffold() { innerPaddings ->
		Box(
			modifier = Modifier.consumeWindowInsets(innerPaddings)
		) {
			CurrentWeatherContent()
		}
	}
}

@Composable
fun CurrentWeatherContent(
) {
	Column(modifier = Modifier.fillMaxSize()) {
		Text("Current Weather")
	}
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherContentPreview() {
	CurrentWeatherContent()
}