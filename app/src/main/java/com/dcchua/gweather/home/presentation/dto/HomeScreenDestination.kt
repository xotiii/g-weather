package com.dcchua.gweather.home.presentation.dto

import com.dcchua.gweather.R

enum class HomeScreenDestination(
	val label: String,
	val icon: Int,
) {
	HOME("Home", R.drawable.ic_home_24dp),
	HISTORY("History", R.drawable.ic_history_24dp),
}