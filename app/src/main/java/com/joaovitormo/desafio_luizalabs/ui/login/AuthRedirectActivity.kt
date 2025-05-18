package com.joaovitormo.desafio_luizalabs.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.joaovitormo.desafio_luizalabs.data.repository.SpotifyAuthRepository
import com.joaovitormo.desafio_luizalabs.MainActivity
import kotlinx.coroutines.launch

class AuthRedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val code = intent?.data?.getQueryParameter("code")
        if (code == null) {
            Log.e("AuthRedirect", "Código ausente na URL de redirecionamento")
            finish()
            return
        }

        val repo = SpotifyAuthRepository(applicationContext)

        lifecycleScope.launch {
            val (token, _) = repo.exchangeCodeForToken(code)
            if (token != null) {
                startActivity(
                    Intent(this@AuthRedirectActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
                finish()
            } else {
                Log.e("AuthRedirect", "Falha ao obter token de acesso")
                finish()
            }
        }
    }
}