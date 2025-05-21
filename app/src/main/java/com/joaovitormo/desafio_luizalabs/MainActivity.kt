package com.joaovitormo.desafio_luizalabs

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.joaovitormo.desafio_luizalabs.databinding.ActivityMainBinding
import com.joaovitormo.desafio_luizalabs.data.local.preferences.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.preferences.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.remote.api.RetrofitInstance
import com.joaovitormo.desafio_luizalabs.data.repository.SpotifyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

    }
}
