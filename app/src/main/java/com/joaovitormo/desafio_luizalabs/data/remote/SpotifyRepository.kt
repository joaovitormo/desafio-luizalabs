package com.joaovitormo.desafio_luizalabs.data.remote

import android.content.Context
import android.util.Log
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistEntity
import com.joaovitormo.desafio_luizalabs.data.model.CreatePlaylistBody
import com.joaovitormo.desafio_luizalabs.data.model.PlaylistResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class SpotifyRepository(
    private val api: SpotifyApiService,
    private val tokenManager: TokenManager,
    private val userPrefs: UserPreferences,
    private val context: Context
) {

    suspend fun fetchAndSaveUserProfile() {
        val token = tokenManager.getAccessToken() ?: return
        val bearer = "Bearer $token"

        val response = api.getCurrentUserProfile(bearer)

        if (response.isSuccessful) {
            val profile = response.body() ?: return

            profile.display_name?.let { userPrefs.saveDisplayName(it) }
            profile.id?.let { userPrefs.saveUserId(it) }


            val imageUrl = profile.images?.firstOrNull { it.height == 300 }?.url
            if (!imageUrl.isNullOrEmpty()) {
                downloadAndSaveImage(context, imageUrl, "")
            }
        } else {
            Log.e("ProfileRepo", "Erro: ${response.code()} - ${response.message()}")
        }
    }

    private suspend fun downloadAndSaveImage(context: Context, url: String, artistId: String?): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) return@withContext null

                val inputStream = response.body?.byteStream() ?: return@withContext null
                var file: File?
                if (artistId.isNullOrEmpty()){
                    file = File(context.filesDir, "profile_image.jpg")

                } else {
                    file = File(context.filesDir, "${artistId}.jpg")

                }
                file.outputStream().use { inputStream.copyTo(it) }

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun createPlaylist(
        token: String,
        userId: String,
        body: CreatePlaylistBody
    ): PlaylistResponse {
        return api.createPlaylist(token, userId, body)
    }

}
