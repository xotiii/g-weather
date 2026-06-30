package com.dcchua.gweather.core.data.repository

import com.dcchua.gweather.core.data.local.dao.UserDao
import com.dcchua.gweather.core.data.local.entity.UserEntity
import com.dcchua.gweather.core.domain.model.User
import com.dcchua.gweather.core.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val userDao: UserDao
) : UserRepository {
	override suspend fun registerUser(username: String, password: String, firstName: String): Long {
		return userDao.insertUser(
			UserEntity(
				username = username,
				password = password,
				firstName = firstName,
			)
		)
	}

	override suspend fun loginUser(username: String, password: String): User? {
		val userEntity = userDao.getUserByUsername(username)
		return if (userEntity != null && userEntity.password == password) {
			User(
				id = userEntity.id,
				username = userEntity.username,
				firstName = userEntity.firstName,
			)
		} else {
			null
		}
	}
}
