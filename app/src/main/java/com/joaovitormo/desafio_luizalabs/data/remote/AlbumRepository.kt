package com.joaovitormo.desafio_luizalabs.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.joaovitormo.desafio_luizalabs.data.local.AlbumEntity
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager

class AlbumsRepository(
    private val artistId: String,
    private val service: SpotifyApiService,
    private val database: AppDatabase,
    private val tokenManager: TokenManager
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAlbumsPagingData() = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = AlbumsRemoteMediator(
            artistId = artistId,
            service = service,
            database = database,
            authHeader = "Bearer ${tokenManager.getAccessToken() ?: ""}"
        ),
        pagingSourceFactory = {
            database.albumsDao().getAlbumsByArtistId(artistId)
        }
    ).liveData
}