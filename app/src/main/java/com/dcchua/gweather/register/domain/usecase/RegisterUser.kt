package com.dcchua.gweather.register.domain.usecase

import com.dcchua.gweather.core.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username: String, password: String, firstName: String) =
        userRepository.registerUser(username, password, firstName)
}
