package com.joaovitormo.desafio_luizalabs.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.joaovitormo.desafio_luizalabs.data.local.entity.AlbumEntity
import com.joaovitormo.desafio_luizalabs.data.local.entity.AlbumsRemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.db.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.remote.api.SpotifyApiService

@OptIn(ExperimentalPagingApi::class)
class AlbumsRemoteMediator(
    private val artistId: String,
    private val service: SpotifyApiService,
    private val database: AppDatabase,
    private val authHeader: String
) : RemoteMediator<Int, AlbumEntity>() {

    private val albumsDao = database.albumsDao()
    private val remoteKeysDao = database.albumsRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AlbumEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = remoteKeysDao.remoteKeysByArtist(artistId)
                remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response = service.getArtistAlbums(
                artistId = artistId,
                authHeader = authHeader,
                offset = offset,
                limit = state.config.pageSize
            )

            val albums = response.body()?.items?.mapNotNull { album ->
                val imageUrl = album.images.firstOrNull()?.url
                val releaseDate = album.release_date // ← novo campo da API

                if (imageUrl != null && releaseDate != null) {
                    AlbumEntity(
                        id = album.id,
                        name = album.name,
                        imageUrl = imageUrl,
                        artistId = artistId,
                        releaseDate = releaseDate
                    )
                } else null
            } ?: emptyList()

            val endReached = albums.size < state.config.pageSize


            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    albumsDao.clearAlbumsByArtist(artistId)
                    remoteKeysDao.clearRemoteKeysByArtist(artistId)
                }

                albumsDao.insertAll(albums)

                val remoteKey = AlbumsRemoteKeys(
                    artistId = artistId,
                    albumId = "",
                    prevKey = if (offset == 0) null else offset - state.config.pageSize,
                    nextKey = if (endReached) null else offset + state.config.pageSize
                )
                remoteKeysDao.insertAll(listOf(remoteKey))
            }

            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            Log.e("AlbumsRemoteMediator", "Erro na paginação: ${e.message}", e)
            return MediatorResult.Error(e)
        }
    }
}