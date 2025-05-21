package com.joaovitormo.desafio_luizalabs.data.remote


import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.PlaylistEntity
import com.joaovitormo.desafio_luizalabs.data.local.PlaylistsRemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

@OptIn(ExperimentalPagingApi::class)
class PlaylistsRemoteMediator(
    private val api: SpotifyApiService,
    private val tokenManager: TokenManager,
    private val database: AppDatabase,
    private val context: Context
) : RemoteMediator<Int, PlaylistEntity>() {

    private val playlistRemoteKeysDao = database.playlistRemoteKeysDao()
    private val playlistsDao = database.playlistsDao()
    private val REMOTE_KEYS_ID = "id"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlaylistEntity>
    ): MediatorResult {
        Log.d("RemoteMediator", "load chamado com tipo: $loadType")

        try {
            val token = tokenManager.getAccessToken()
            val remoteKeys = playlistRemoteKeysDao.remoteKeysByPlaylist(REMOTE_KEYS_ID)
            val offset: Int = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.APPEND -> {
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = api.getPlaylists("Bearer $token", limit = state.config.pageSize, offset = offset)
            val playlists = response.body()?.items ?: emptyList()

            val endOfPaginationReached = playlists.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    playlistRemoteKeysDao.clearRemoteKeysByPlaylist(REMOTE_KEYS_ID)
                    playlistsDao.clearAll()
                }

                playlistRemoteKeysDao.insertOrReplace(
                    PlaylistsRemoteKeys(playlistId = REMOTE_KEYS_ID, nextKey = (offset + playlists.size), prevKey = offset)
                )

                val playlistEntities = playlists.map { playlist ->
                    val imageUrl = playlist.images?.firstOrNull()?.url
                    val localImagePath = try {
                        imageUrl?.let { downloadAndSaveImage(context, it, playlist.id) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }

                    PlaylistEntity(
                        id = playlist.id,
                        name = playlist.name,
                        imageUrl = localImagePath ?: "",
                        ownerName = playlist.owner.display_name
                    )
                }


                playlistsDao.insertAll(playlistEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            Log.e("RemoteMediator", "Erro no load: ${e.message}", e)
            return MediatorResult.Error(e)
        }
    }


    private suspend fun downloadAndSaveImage(context: Context, url: String, playlistId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@withContext null

                val inputStream = response.body?.byteStream() ?: return@withContext null
                val file = File(context.filesDir, "$playlistId.jpg")
                file.outputStream().use { inputStream.copyTo(it) }

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

