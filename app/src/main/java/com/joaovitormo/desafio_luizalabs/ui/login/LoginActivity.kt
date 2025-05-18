package com.joaovitormo.desafio_luizalabs.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.joaovitormo.desafio_luizalabs.MainActivity
import com.joaovitormo.desafio_luizalabs.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (viewModel.isTokenValid()) {
            goToMain()
            return
        }


        binding.loginButton.setOnClickListener {
            viewModel.startLogin()
        }

        viewModel.authUrl.observe(this) { uri ->
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        viewModel.loginSuccess.observe(this) { success ->
            if (success) {
                goToMain()
            } else {
                Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
