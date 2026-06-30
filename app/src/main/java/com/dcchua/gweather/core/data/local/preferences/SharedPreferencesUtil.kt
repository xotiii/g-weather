package com.dcchua.gweather.core.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import com.dcchua.gweather.core.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val USER_KEY = "USER_KEY"

class SharedPreferencesUtil @Inject constructor(@ApplicationContext context: Context) {

	private val sharedPreferences =
		context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

	private fun saveData(key: String, value: String?) {
		sharedPreferences.edit {
			putString(key, value)
		}
	}

	fun saveUser(user: User) {
		val userJson = Json.encodeToString(user)
		saveData(USER_KEY, userJson)
	}

	fun getUser(): User? {
		val userJson = sharedPreferences.getString(USER_KEY, null) ?: return null
		return try {
			Json.decodeFromString<User>(userJson)
		} catch (_: Exception) {
			null
		}
	}

	fun clearUser() {
		sharedPreferences.edit { remove(USER_KEY) }
	}

}