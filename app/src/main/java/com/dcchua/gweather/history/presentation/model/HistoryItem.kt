package com.dcchua.gweather.history.presentation.model

import androidx.annotation.DrawableRes

data class HistoryItem(
	@DrawableRes val iconId: Int,
	val location: String,
	val date: String,
	val temperature: String,
	val sunrise: String,
	val sunset: String,
)
