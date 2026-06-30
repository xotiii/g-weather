package com.dcchua.gweather.history.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcchua.gweather.R
import com.dcchua.gweather.history.presentation.model.HistoryItem

@Composable
fun HistoryScreen(
	viewModel: HistoryViewModel = hiltViewModel(),
) {

	val historyList by viewModel.historyList.collectAsStateWithLifecycle(emptyList())

	Scaffold { innerPaddings ->
		Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
			HistoryContent(historyItems = historyList)
		}
	}
}

@Composable
fun HistoryContent(
	historyItems: List<HistoryItem>
) {
	LazyColumn (
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize(),
	) {
		items(items = historyItems) {
			HistoryItemContent(historyItem = it)
			Spacer(modifier = Modifier.size(16.dp))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun HistoryContentPreview() {
	HistoryContent(
		historyItems = listOf(
			HistoryItem(
				iconId = R.drawable.clear_sun,
				location = "Taguig City, PH",
				date = "June 26, 2026",
				temperature = "25.5",
				sunrise = "5:30 AM",
				sunset = "5:30 PM",
			),
			HistoryItem(
				iconId = R.drawable.clear_sun,
				location = "Taguig City, PH",
				date = "June 26, 2026",
				temperature = "25.5",
				sunrise = "5:30 AM",
				sunset = "5:30 PM",
			),
			HistoryItem(
				iconId = R.drawable.clear_sun,
				location = "Taguig City, PH",
				date = "June 26, 2026",
				temperature = "25.5",
				sunrise = "5:30 AM",
				sunset = "5:30 PM",
			),
		)
	)
}