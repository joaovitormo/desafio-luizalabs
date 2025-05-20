package com.joaovitormo.desafio_luizalabs.data.remote

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.RemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistDao
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.HttpException
import java.io.File

@OptIn(ExperimentalPagingApi::class)
class TopArtistsRemoteMediator(
    private val api: SpotifyApiService,
    private val tokenManager: TokenManager,
    private val database: AppDatabase,
    private val context: Context
) : RemoteMediator<Int, TopArtistEntity>() {

    private val remoteKeysDao = database.remoteKeysDao()
    private val topArtistDao = database.topArtistDao()
    private val REMOTE_KEYS_ID = "top_artists" // chave fixa pois só tem um endpoint

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TopArtistEntity>
    ): MediatorResult {
        Log.d("RemoteMediator", "load chamado com tipo: $loadType")

        try {
            val token = tokenManager.getAccessToken()
            val remoteKeys = remoteKeysDao.getRemoteKeyById(REMOTE_KEYS_ID)
            val offset: Int = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.APPEND -> {
                    remoteKeys?.nextOffset ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = api.getTopArtists("Bearer $token", limit = state.config.pageSize, offset = offset)
            val artists = response.body()?.items ?: emptyList()

            val endOfPaginationReached = artists.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteById(REMOTE_KEYS_ID)
                    topArtistDao.clearAll()
                }

                remoteKeysDao.insertOrReplace(
                    RemoteKeys(id = REMOTE_KEYS_ID, nextOffset = (offset + artists.size))
                )

                val artistEntities = artists.map { artist ->
                    val imageUrl = artist.images.firstOrNull()?.url
                    val localImagePath = imageUrl?.let { downloadAndSaveImage(context, it, artist.id) }

                    TopArtistEntity(
                        id = artist.id,
                        name = artist.name,
                        imagePath = localImagePath ?: ""
                    )
                }

                topArtistDao.insertAll(artistEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            Log.e("RemoteMediator", "Erro no load: ${e.message}", e)
            return MediatorResult.Error(e)
        }
    }


    private suspend fun downloadAndSaveImage(context: Context, url: String, artistId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@withContext null

                val inputStream = response.body?.byteStream() ?: return@withContext null
                val file = File(context.filesDir, "$artistId.jpg")
                file.outputStream().use { inputStream.copyTo(it) }

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

