package com.dcchua.gweather.register.presentation.state

sealed class RegisterUiState {

	data object Initial: RegisterUiState()
	data object Success: RegisterUiState()

	data object Loading: RegisterUiState()
	sealed class Error: RegisterUiState() {
		data object UsernameExist: Error()
		data object Unknown: Error()
	}

}