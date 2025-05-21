package com.joaovitormo.desafio_luizalabs.ui.artists

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.joaovitormo.desafio_luizalabs.data.local.db.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.preferences.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.preferences.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.remote.api.RetrofitInstance
import com.joaovitormo.desafio_luizalabs.data.repository.SpotifyRepository
import com.joaovitormo.desafio_luizalabs.data.repository.TopArtistsRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.File

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val database = AppDatabase.getInstance(context)
    private val tokenManager = TokenManager(context)
    private val api = RetrofitInstance.api

    private val _profileImageFile = MutableLiveData<File?>()
    val profileImageFile: LiveData<File?> get() = _profileImageFile

    init {
        loadProfileImage()
    }

    private fun loadProfileImage() {
        val repository = SpotifyRepository(
            api = RetrofitInstance.api,
            tokenManager = TokenManager(context),
            userPrefs = UserPreferences(context),
            context = context
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.fetchAndSaveUserProfile()

            val imageFile = File(getApplication<Application>().filesDir, "profile_image.jpg")
            if (imageFile.exists()) {
                _profileImageFile.postValue(imageFile)
            } else {
                _profileImageFile.postValue(null)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    val pagingData = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = TopArtistsRemoteMediator(api, tokenManager, database, context),
        pagingSourceFactory = {
            database.topArtistDao().getPagedArtists()
        }
    ).liveData.cachedIn(viewModelScope)
}

