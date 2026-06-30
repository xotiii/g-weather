package com.dcchua.gweather.core.domain.repository

import com.dcchua.gweather.core.domain.model.User

interface UserRepository {
    suspend fun registerUser(username: String, password: String, firstName: String): Long
    suspend fun loginUser(username: String, password: String): User?
}
