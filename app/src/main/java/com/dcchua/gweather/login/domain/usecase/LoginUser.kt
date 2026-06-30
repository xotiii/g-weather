package com.dcchua.gweather.login.domain.usecase

import com.dcchua.gweather.core.data.local.preferences.SharedPreferencesUtil
import com.dcchua.gweather.core.domain.repository.UserRepository
import javax.inject.Inject

class LoginUser @Inject constructor(
	private val userRepository: UserRepository,
	private val sharedPreferencesUtil: SharedPreferencesUtil
) {
    suspend operator fun invoke(username: String, password: String): Boolean {
        val user = userRepository.loginUser(username, password)
        return if (user != null) {
            sharedPreferencesUtil.saveUser(user)
            true
        } else {
            false
        }
    }
}