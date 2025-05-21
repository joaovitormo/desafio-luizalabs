package com.joaovitormo.desafio_luizalabs.ui.playlists

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joaovitormo.desafio_luizalabs.data.local.db.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.preferences.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.preferences.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.repository.SpotifyRepository

class PlaylistsViewModelFactory(
    private val repository: SpotifyRepository,
    private val tokenManager: TokenManager,
    private val userPreferences: UserPreferences,
    private val database: AppDatabase,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistsViewModel(repository, tokenManager, userPreferences, database, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}