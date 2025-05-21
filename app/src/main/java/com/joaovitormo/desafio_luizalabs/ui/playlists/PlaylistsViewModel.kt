package com.joaovitormo.desafio_luizalabs.ui.playlists

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.PlaylistEntity
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.model.CreatePlaylistBody
import com.joaovitormo.desafio_luizalabs.data.remote.PlaylistsRemoteMediator
import com.joaovitormo.desafio_luizalabs.data.remote.RetrofitInstance
import com.joaovitormo.desafio_luizalabs.data.remote.SpotifyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val database = AppDatabase.getInstance(context)
    private val tokenManager = TokenManager(context)
    private val api = RetrofitInstance.api
    private val userPreferences = UserPreferences(context)

    val token = tokenManager.getAccessToken()
    val userId = userPreferences.getUserId()

    private val repository = SpotifyRepository(api, tokenManager,userPreferences, context)


    private val _profileImageFile = MutableLiveData<File?>()
    val profileImageFile: LiveData<File?> get() = _profileImageFile

    private val _pagingData = MutableLiveData<Pager<Int, PlaylistEntity>>()
    val pagingData = _pagingData.switchMap { pager ->
        pager.liveData.cachedIn(viewModelScope)
    }

    init {
        loadProfileImage()
        loadPlaylists()
    }

    private fun loadProfileImage() {
        val imageFile = File(getApplication<Application>().filesDir, "profile_image.jpg")
        if (imageFile.exists()) {
            _profileImageFile.postValue(imageFile)
        } else {
            _profileImageFile.postValue(null)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun loadPlaylists() {
        val pager = Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PlaylistsRemoteMediator(api, tokenManager, database, context),
            pagingSourceFactory = {
                database.playlistsDao().getPagedPlaylists()
            }
        )
        _pagingData.postValue(pager)
    }

    fun createPlaylist(
        userId: String,
        token: String,
        playlistName: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.createPlaylist(
                    token = "Bearer $token",
                    userId = userId,
                    body = CreatePlaylistBody(name = playlistName)
                )
                Log.d("CreatePlaylist", "Criada: ${response.name} (ID: ${response.id})")
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("CreatePlaylist", "Erro ao criar playlist: ${e.message}")
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}