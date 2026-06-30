package com.dcchua.gweather.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcchua.gweather.login.presentation.state.LoginUiState

@Composable
fun LoginScreen(
	onNavigateToHome: () -> Unit,
	onNavigateToRegister: () -> Unit,
	viewModel: LoginViewModel = hiltViewModel(),
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()
	
	var username by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	if (uiState is LoginUiState.Success) onNavigateToHome()

	Scaffold { innerPaddings ->
		Box(modifier = Modifier.consumeWindowInsets(innerPaddings)) {
			LoginContent(
				uiState = uiState,
				username = username,
				onUsernameChange = { username = it },
				password = password,
				onPasswordChange = { password = it },
				onClickLogin = { viewModel.login(username, password) },
				onClickRegister = onNavigateToRegister,
			)
		}
	}

	if (uiState is LoginUiState.Error) {
		AlertDialog(
			onDismissRequest = { viewModel.resetState() },
			title = { Text("Error") },
			text = { Text("Please check your username or password") },
			confirmButton = {
				TextButton(onClick = { viewModel.resetState() }) {
					Text("OK")
				}
			}
		)
	}

}

@Composable
fun LoginContent(
	uiState: LoginUiState,
	username: String,
	onUsernameChange: (String) -> Unit,
	password: String,
	onPasswordChange: (String) -> Unit,
	onClickLogin: () -> Unit,
	onClickRegister: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		OutlinedTextField(
			value = username,
			label = { Text("Username") },
			onValueChange = onUsernameChange,
			enabled = uiState !is LoginUiState.Loading
		)
		Spacer(modifier = Modifier.size(16.dp))
		OutlinedTextField(
			value = password,
			label = { Text("Password") },
			onValueChange = onPasswordChange,
			visualTransformation = PasswordVisualTransformation(),
			enabled = uiState !is LoginUiState.Loading
		)
		Spacer(modifier = Modifier.size(16.dp))
		Button(
			onClick = onClickLogin,
			enabled = uiState !is LoginUiState.Loading
					&& username.isNotBlank()
					&& password.isNotBlank()
		) {
			if (uiState is LoginUiState.Loading) {
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary,
					strokeWidth = 2.dp
				)
			} else {
				Text("Log In")
			}
		}
		TextButton(
			onClick = onClickRegister,
			enabled = uiState !is LoginUiState.Loading,
		) {
			Text("Don't have an account? Register here")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
	LoginContent(
		uiState = LoginUiState.Initial,
		username = "",
		onUsernameChange = {},
		password = "",
		onPasswordChange = {},
		onClickLogin = {},
		onClickRegister = {},
	)
}