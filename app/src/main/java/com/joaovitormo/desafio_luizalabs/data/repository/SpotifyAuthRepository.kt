package com.joaovitormo.desafio_luizalabs.data.repository

import android.content.Context
import android.net.Uri
import com.joaovitormo.desafio_luizalabs.data.local.preferences.TokenManager
import com.joaovitormo.desafio_luizalabs.util.PKCEUtil
import com.joaovitormo.desafio_luizalabs.BuildConfig
import com.joaovitormo.desafio_luizalabs.data.remote.api.SpotifyAuthApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyAuthRepository(
    private val context: Context
) {
    object SpotifyAuthConfig {
        const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
        const val REDIRECT_URI = BuildConfig.SPOTIFY_REDIRECT_URI
        const val AUTH_URL = "https://accounts.spotify.com/authorize"
        val SCOPES = listOf("user-read-email", "user-read-private", "user-library-read", "user-top-read", "playlist-modify-public", "playlist-modify-private", "playlist-read-private")
        const val RESPONSE_TYPE = "code"
        const val CODE_CHALLENGE_METHOD = "S256"
    }

    private val tokenManager = TokenManager(context)

    private val authApi: SpotifyAuthApi by lazy {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SpotifyAuthApi::class.java)
    }

    fun buildAuthUrl(): Uri {
        val verifier = PKCEUtil.generateCodeVerifier()
        val challenge = PKCEUtil.generateCodeChallenge(verifier)
        tokenManager.saveCodeVerifier(verifier)

        return Uri.parse(SpotifyAuthConfig.AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", SpotifyAuthConfig.CLIENT_ID)
            .appendQueryParameter("response_type", SpotifyAuthConfig.RESPONSE_TYPE)
            .appendQueryParameter("redirect_uri", SpotifyAuthConfig.REDIRECT_URI)
            .appendQueryParameter("code_challenge_method", SpotifyAuthConfig.CODE_CHALLENGE_METHOD)
            .appendQueryParameter("code_challenge", challenge)
            .appendQueryParameter("scope", SpotifyAuthConfig.SCOPES.joinToString(" "))
            .build()
    }

    suspend fun exchangeCodeForToken(code: String): Pair<String?, Long?> {
        val verifier = tokenManager.getCodeVerifier() ?: return Pair(null, null)

        val response = authApi.getToken(
            clientId = SpotifyAuthConfig.CLIENT_ID,
            grantType = "authorization_code",
            code = code,
            redirectUri = SpotifyAuthConfig.REDIRECT_URI,
            codeVerifier = verifier
        )

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                tokenManager.saveToken(body.access_token, body.expires_in)
                return Pair(body.access_token, body.expires_in)
            }
        }
        return Pair(null, null)
    }

    fun getAccessToken(): String? = tokenManager.getAccessToken()
    fun isTokenValid(): Boolean = tokenManager.isTokenValid()
}

