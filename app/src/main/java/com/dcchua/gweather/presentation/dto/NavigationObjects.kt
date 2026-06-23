package com.dcchua.gweather.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationObjects {
	@Serializable
	data object Login : NavigationObjects()
	@Serializable
	data object Register : NavigationObjects()
	@Serializable
	data object Home : NavigationObjects()
}