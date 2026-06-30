package com.dcchua.gweather.register.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcchua.gweather.R
import com.dcchua.gweather.presentation.theme.GWeatherTheme
import com.dcchua.gweather.register.presentation.state.RegisterUiState

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(
	onNavigateBack: () -> Unit,
	viewModel: RegisterViewModel = hiltViewModel()
) {
	val uiState by viewModel.uiState.collectAsStateWithLifecycle()

	var username by remember { mutableStateOf("") }
	var firstName by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var confirmPassword by remember { mutableStateOf("") }

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
		Box(
			modifier = Modifier
				.padding(innerPaddings)
				.fillMaxSize()
		) {
			RegisterContent(
				uiState = uiState,
				username = username,
				onUsernameChange = { username = it },
				firstName = firstName,
				onFirstNameChange = { firstName = it },
				password = password,
				onPasswordChange = { password = it },
				confirmPassword = confirmPassword,
				onConfirmPasswordChange = { confirmPassword = it },
				onClickCreateAccount = {
					if (password == confirmPassword) {
						viewModel.register(username, password, firstName)
					}
				}
			)
		}
	}

	when (uiState) {
		is RegisterUiState.Success -> AlertDialog(
			onDismissRequest = { /* No-op to force action */ },
			title = { Text("Success") },
			text = { Text("Register successful") },
			confirmButton = {
				TextButton(onClick = onNavigateBack) {
					Text("OK")
				}
			}
		)

		is RegisterUiState.Error -> AlertDialog(
			onDismissRequest = { viewModel.resetState() },
			title = { Text("Error") },
			text = {
				Text(
					when (uiState) {
						RegisterUiState.Error.UsernameExist -> "Username already exist"
						else -> "Unknown error"
					}
				)
			},
			confirmButton = {
				TextButton(onClick = { viewModel.resetState() }) {
					Text("OK")
				}
			}
		)

		else -> {}
	}
}

@Composable
fun RegisterContent(
	uiState: RegisterUiState,
	username: String,
	onUsernameChange: (String) -> Unit,
	firstName: String,
	onFirstNameChange: (String) -> Unit,
	password: String,
	onPasswordChange: (String) -> Unit,
	confirmPassword: String,
	onConfirmPasswordChange: (String) -> Unit,
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
				value = username,
				label = { Text("Username") },
				onValueChange = onUsernameChange,
				enabled = uiState !is RegisterUiState.Loading
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = firstName,
				label = { Text("First Name") },
				onValueChange = onFirstNameChange,
				enabled = uiState !is RegisterUiState.Loading
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = password,
				label = { Text("Password") },
				onValueChange = onPasswordChange,
				visualTransformation = PasswordVisualTransformation(),
				enabled = uiState !is RegisterUiState.Loading
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = confirmPassword,
				label = { Text("Confirm Password") },
				onValueChange = onConfirmPasswordChange,
				visualTransformation = PasswordVisualTransformation(),
				enabled = uiState !is RegisterUiState.Loading
			)
			if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
				Text(
					text = "Passwords do not match",
					color = MaterialTheme.colorScheme.error,
					style = MaterialTheme.typography.bodySmall
				)
			}
		}
		Button(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			onClick = onClickCreateAccount,
			enabled = uiState !is RegisterUiState.Loading &&
					username.isNotBlank() &&
					firstName.isNotBlank() &&
					password.isNotBlank() &&
					confirmPassword.isNotBlank()
		) {
			if (uiState is RegisterUiState.Loading) {
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary,
					strokeWidth = 2.dp
				)
			} else {
				Text("Create Account")
			}
		}
	}

}

@Preview(showBackground = true)
@Composable
fun RegisterContentPreview() {
	GWeatherTheme {
		RegisterContent(
			uiState = RegisterUiState.Initial,
			username = "",
			onUsernameChange = {},
			firstName = "",
			onFirstNameChange = {},
			password = "",
			onPasswordChange = {},
			confirmPassword = "",
			onConfirmPasswordChange = {},
			onClickCreateAccount = {},
		)
	}
}