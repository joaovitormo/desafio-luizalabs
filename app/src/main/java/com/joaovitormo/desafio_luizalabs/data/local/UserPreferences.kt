package com.joaovitormo.desafio_luizalabs.data.local

import android.content.Context

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveDisplayName(name: String) {
        prefs.edit().putString("display_name", name).apply()
    }

    fun getDisplayName(): String? {
        return prefs.getString("display_name", null)
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString("user_id", userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun clearUserData() {
        return prefs.edit().clear().apply()
    }
}
