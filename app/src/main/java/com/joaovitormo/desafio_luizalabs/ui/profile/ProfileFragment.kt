package com.joaovitormo.desafio_luizalabs.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.joaovitormo.desafio_luizalabs.data.local.TokenManager
import com.joaovitormo.desafio_luizalabs.data.remote.SpotifyRepository
import com.joaovitormo.desafio_luizalabs.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.data.local.UserPreferences
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userPrefs = UserPreferences(requireContext())
        val name = userPrefs.getDisplayName()
        binding.textName.text = name

        val imageFile = File(requireContext().filesDir, "profile_image.jpg")
        if (imageFile.exists()) {
            Glide.with(this)
                .load(imageFile)
                .circleCrop()
                .into(binding.imageProfile)
        }
    }
}