package com.dcchua.gweather.login.presentation.state

sealed class LoginUiState {
	data object Initial : LoginUiState()
	data object Loading : LoginUiState()
	data object Success : LoginUiState()
	data object Error : LoginUiState()
}