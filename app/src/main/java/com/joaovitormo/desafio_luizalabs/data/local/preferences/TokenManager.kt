package com.joaovitormo.desafio_luizalabs.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "spotify_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_EXPIRES_AT    = "expires_at"
        private const val KEY_VERIFIER      = "code_verifier"
    }

    fun saveCodeVerifier(verifier: String) {
        prefs.edit().putString(KEY_VERIFIER, verifier).apply()
    }

    fun getCodeVerifier(): String? =
        prefs.getString(KEY_VERIFIER, null)

    fun saveToken(token: String, expiresIn: Long) {
        val expiry = System.currentTimeMillis() + expiresIn * 1000
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .putLong(KEY_EXPIRES_AT, expiry)
            .apply()
    }

    fun getAccessToken(): String? {
        val expiry = prefs.getLong(KEY_EXPIRES_AT, 0L)
        return if (System.currentTimeMillis() < expiry) {
            prefs.getString(KEY_ACCESS_TOKEN, null)
        } else null
    }
    fun isTokenValid(): Boolean {
        val expiry = prefs.getLong(KEY_EXPIRES_AT, 0L)
        val token = prefs.getString(KEY_ACCESS_TOKEN, null)
        return token != null && System.currentTimeMillis() < expiry
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

}
