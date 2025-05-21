package com.joaovitormo.desafio_luizalabs.ui.artistDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.remote.AlbumsRemoteMediator
import com.joaovitormo.desafio_luizalabs.data.remote.RetrofitInstance

@OptIn(ExperimentalPagingApi::class)
class ArtistDetailViewModel(
    application: Application,
    private val artistId: String
) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val database = AppDatabase.getInstance(context)
    private val tokenManager = TokenManager(context)
    private val api = RetrofitInstance.api

    val albumsPagingData = Pager(
        config = PagingConfig(pageSize = 20,
            enablePlaceholders = false),
        remoteMediator = AlbumsRemoteMediator(
            artistId = artistId,
            service = api,
            database = database,
            authHeader = "Bearer ${tokenManager.getAccessToken() ?: ""}"
        ),
        pagingSourceFactory = { database.albumsDao().getAlbumsByArtistId(artistId) }
    ).liveData.cachedIn(viewModelScope)
}
