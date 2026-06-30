package com.dcchua.gweather.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcchua.gweather.core.util.coroutines.coRunCatching
import com.dcchua.gweather.login.domain.usecase.LoginUser
import com.dcchua.gweather.login.presentation.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val loginUser: LoginUser
): ViewModel() {

	private var loginJob: Job? = null

	private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
	val uiState = _uiState.asStateFlow()

	fun login(username: String, password: String) {
		if (loginJob?.isActive == true) return
		loginJob = viewModelScope.launch {
			_uiState.value = LoginUiState.Loading
			coRunCatching {
				loginUser(username, password)
			}.onSuccess {
				_uiState.value = if (it) {
					LoginUiState.Success
				} else {
					LoginUiState.Error
				}
			}.onFailure {
				_uiState.value = LoginUiState.Error
			}
		}
	}

	fun resetState() {
		_uiState.value = LoginUiState.Initial
	}

}