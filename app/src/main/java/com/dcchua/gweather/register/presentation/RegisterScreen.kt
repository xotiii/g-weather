package com.dcchua.gweather.register.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcchua.gweather.R
import com.dcchua.gweather.presentation.theme.GWeatherTheme

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
	onNavigateBack: () -> Unit,
) {
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Register Account") },
				navigationIcon = {
					IconButton(onClick = onNavigateBack) {
						Icon(
							painter = painterResource(R.drawable.ic_chevron_back_24dp),
							contentDescription = "Back"
						)
					}
				}
			)
		}
	) { innerPaddings ->
		Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
			RegisterContent(onClickCreateAccount = onNavigateBack)
		}
	}
}

@Composable
fun RegisterContent(
	onClickCreateAccount: () -> Unit,
) {
	Column(modifier = Modifier.fillMaxSize()) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = "",
				label = { Text("Email") },
				onValueChange = {},
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = "",
				label = { Text("Password") },
				onValueChange = {},
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = "",
				label = { Text("Confirm Password") },
				onValueChange = {},
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = "",
				label = { Text("First Name") },
				onValueChange = {},
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = "",
				label = { Text("Last Name") },
				onValueChange = {},
			)
		}
		Button(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			onClick = onClickCreateAccount,
		) {
			Text("Create Account")
		}
	}

}

@Preview(showBackground = true)
@Composable
fun RegisterContentPreview() {
	GWeatherTheme {
		RegisterContent(
			onClickCreateAccount = {},
		)
	}
}