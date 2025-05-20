package com.joaovitormo.desafio_luizalabs

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.joaovitormo.desafio_luizalabs.databinding.ActivityMainBinding
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.local.UserPreferences
import com.joaovitormo.desafio_luizalabs.data.remote.RetrofitInstance
import com.joaovitormo.desafio_luizalabs.data.remote.SpotifyRepository
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


        val repository = SpotifyRepository(
            api = RetrofitInstance.api,
            tokenManager = TokenManager(applicationContext),
            userPrefs = UserPreferences(applicationContext),
            context = applicationContext
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.fetchAndSaveUserProfile()
        }
    }
}
