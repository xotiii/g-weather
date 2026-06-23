package com.dcchua.gweather.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.dcchua.gweather.current.presentation.CurrentWeatherScreen
import com.dcchua.gweather.history.presentation.HistoryScreen
import com.dcchua.gweather.home.presentation.dto.HomeScreenDestination

@Composable
fun HomeScreen() {
	var currentDestination by rememberSaveable { mutableStateOf(HomeScreenDestination.HOME) }

	NavigationSuiteScaffold(
		navigationSuiteItems = {
			HomeScreenDestination.entries.forEach {
				item(
					icon = {
						Icon(
							painterResource(it.icon),
							contentDescription = it.label,
						)
					},
					label = { Text(it.label) },
					selected = it == currentDestination,
					onClick = { currentDestination = it }
				)
			}
		}
	) {
		Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
			Box(modifier = Modifier.consumeWindowInsets(innerPadding)) {
				when (currentDestination) {
					HomeScreenDestination.HOME -> CurrentWeatherScreen()
					HomeScreenDestination.HISTORY -> HistoryScreen()
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
	HomeScreen()
}