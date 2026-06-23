package com.dcchua.gweather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dcchua.gweather.home.presentation.HomeScreen
import com.dcchua.gweather.login.presentation.LoginScreen
import com.dcchua.gweather.presentation.dto.NavigationObjects
import com.dcchua.gweather.presentation.theme.GWeatherTheme
import com.dcchua.gweather.register.presentation.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			GWeatherTheme {
				GWeatherApp()
			}
		}
	}
}

@ExperimentalMaterial3Api
@PreviewScreenSizes
@Composable
fun GWeatherApp() {
	GWeatherTheme {
		val navController = rememberNavController()
		NavHost(
			navController = navController,
			startDestination = NavigationObjects.Login,
		) {
			composable<NavigationObjects.Login> {
				LoginScreen(
					onNavigateToLogin = { navController.navigate(NavigationObjects.Home) },
					onNavigateToRegister = { navController.navigate(NavigationObjects.Register) }
				)
			}
			composable<NavigationObjects.Register> {
				RegisterScreen(
					onNavigateBack = { navController.popBackStack() }
				)
			}
			composable<NavigationObjects.Home> { HomeScreen() }
		}
	}

}
