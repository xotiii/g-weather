package com.dcchua.gweather.register.presentation

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcchua.gweather.core.util.coroutines.coRunCatching
import com.dcchua.gweather.register.domain.usecase.RegisterUser
import com.dcchua.gweather.register.presentation.state.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUser: RegisterUser
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = RegisterUiState.Initial
    }

    fun register(username: String, password: String, firstName: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            coRunCatching {
                registerUser(username, password, firstName)
            }.onSuccess {
                _uiState.value = RegisterUiState.Success
            }.onFailure {
                _uiState.value = when(it) {
                    is SQLiteConstraintException -> RegisterUiState.Error.UsernameExist
                    else -> RegisterUiState.Error.Unknown
                }
            }
        }
    }
}
