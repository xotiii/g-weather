package com.dcchua.gweather.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
	onNavigateToLogin: () -> Unit,
	onNavigateToRegister: () -> Unit,
) {
	Scaffold { innerPaddings ->
		Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
			LoginContent(
				onClickLogin = onNavigateToLogin,
				onClickRegister = onNavigateToRegister,
			)
		}
	}
}

@Composable
fun LoginContent(
	onClickLogin: () -> Unit,
	onClickRegister: () -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		OutlinedTextField(
			value = "",
			label = { Text("Email") },
			onValueChange = {},
		)
		Spacer(modifier = Modifier.size(16.dp))
		OutlinedTextField(
			value = "",
			label = { Text("Password") },
			onValueChange = {},
		)
		Spacer(modifier = Modifier.size(16.dp))
		Button(onClick = onClickLogin) {
			Text("Log In")
		}
		TextButton(onClick = onClickRegister) {
			Text("Don't have an account? Register here")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
	LoginContent(
		onClickLogin = {},
		onClickRegister = {},
	)
}