package com.joaovitormo.desafio_luizalabs.ui.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.remote.RetrofitInstance
import com.joaovitormo.desafio_luizalabs.data.remote.TopArtistsRemoteMediator

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
        val imageFile = File(getApplication<Application>().filesDir, "profile_image.jpg")
        if (imageFile.exists()) {
            _profileImageFile.postValue(imageFile)
        } else {
            _profileImageFile.postValue(null)
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

