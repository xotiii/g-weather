package com.dcchua.gweather.history.presentation

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
fun HistoryScreen() {
	Scaffold { innerPaddings ->
		Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
			HistoryContent()
		}
	}
}

@Composable
fun HistoryContent() {
	Column(modifier = Modifier.fillMaxSize()) {
		Text("History")
	}
}

@Preview(showBackground = true)
@Composable
fun HistoryContentPreview() {
	HistoryContent()
}