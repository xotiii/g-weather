package com.dcchua.gweather.core.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val USER_ID_KEY = "USER_ID_KEY"

class SharedPreferencesUtil @Inject constructor(@ApplicationContext context: Context) {

	private val sharedPreferences =
		context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

	private fun saveData(key: String, value: String?) {
		sharedPreferences.edit {
			putString(key, value)
		}
	}

	fun saveUserId(userId: String) {
		saveData(USER_ID_KEY, userId)
	}

	fun getUserId(): String? = sharedPreferences.getString(USER_ID_KEY, null)

	fun clearUserId() {
		sharedPreferences.edit { remove(USER_ID_KEY) }
	}

}