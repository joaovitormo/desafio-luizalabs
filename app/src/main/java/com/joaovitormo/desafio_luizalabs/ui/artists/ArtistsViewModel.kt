package com.joaovitormo.desafio_luizalabs.ui.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).topArtistDao()

    private val _artists = MutableLiveData<List<TopArtistEntity>>()
    val artists: LiveData<List<TopArtistEntity>> = _artists

    fun loadArtists() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dao.getAll()
            _artists.postValue(result)
        }
    }
}