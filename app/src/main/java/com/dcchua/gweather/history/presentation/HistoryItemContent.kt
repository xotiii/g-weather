package com.dcchua.gweather.history.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcchua.gweather.R
import com.dcchua.gweather.history.presentation.model.HistoryItem

@Composable
fun HistoryItemContent(
	historyItem: HistoryItem,
) {
	OutlinedCard(
		modifier = Modifier.fillMaxWidth(),
	) {
		Column {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween,
			) {
				Image(
					painter = painterResource(historyItem.iconId),
					modifier = Modifier.size(48.dp),
					contentDescription = "",
				)
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Text(
						text = historyItem.location,
						style = MaterialTheme.typography.labelLarge
					)
					Text(historyItem.date)
				}
				Text(
					text = stringResource(
						R.string.current_weather_temperature_celsius,
						historyItem.temperature,
					),
					style = MaterialTheme.typography.titleLarge
				)
			}
			HorizontalDivider(
				modifier = Modifier.padding(horizontal = 16.dp)
			)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceEvenly,
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Image(
						modifier = Modifier.size(24.dp),
						painter = painterResource(R.drawable.ic_sunrise),
						contentDescription = ""
					)
					Spacer(modifier = Modifier.size(8.dp))
					Text(historyItem.sunrise)
				}
				Row(verticalAlignment = Alignment.CenterVertically) {
					Image(
						modifier = Modifier.size(24.dp),
						painter = painterResource(R.drawable.ic_sunset),
						contentDescription = ""
					)
					Spacer(modifier = Modifier.size(8.dp))
					Text(historyItem.sunset)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun HistoryItemContentPreview() {
	HistoryItemContent(
		historyItem = HistoryItem(
			iconId = R.drawable.clear_sun,
			location = "Taguig City, PH",
			date = "June 26, 2026",
			temperature = "25.5",
			sunrise = "5:30 AM",
			sunset = "5:30 PM",
		),
	)
}