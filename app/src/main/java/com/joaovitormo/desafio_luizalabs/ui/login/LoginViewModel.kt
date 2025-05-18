package com.joaovitormo.desafio_luizalabs.ui.login

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joaovitormo.desafio_luizalabs.data.repository.SpotifyAuthRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SpotifyAuthRepository(
        application.applicationContext
    )

    private val _authUrl = MutableLiveData<Uri>()
    val authUrl: LiveData<Uri> = _authUrl

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun startLogin() {
        _authUrl.value = repository.buildAuthUrl()
    }

    fun isTokenValid(): Boolean = repository.isTokenValid()
}
