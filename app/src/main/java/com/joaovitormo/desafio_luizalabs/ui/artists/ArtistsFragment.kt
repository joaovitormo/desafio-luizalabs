package com.joaovitormo.desafio_luizalabs.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.data.local.AppDatabase
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistDao
import com.joaovitormo.desafio_luizalabs.databinding.FragmentArtistsBinding
import java.io.File

class ArtistsFragment : Fragment() {
    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArtistsViewModel
    private lateinit var adapter: ArtistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ArtistsViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadArtists()

        val imageFile = File(requireContext().filesDir, "profile_image.jpg")
        if (imageFile.exists()) {
            Glide.with(this)
                .load(imageFile)
                .circleCrop()
                .into(binding.imageProfile)
        }
    }

    private fun setupRecyclerView() {
        adapter = ArtistsAdapter(emptyList()) { artist ->
            showArtistaDetails(artist.name) // Assumindo que TopArtistEntity tem o campo name
        }

        binding.recyclerViewArtists.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@ArtistsFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun observeViewModel() {
        viewModel.artists.observe(viewLifecycleOwner) { artists ->
            adapter.updateData(artists)
        }
    }

    private fun showArtistaDetails(artista: String) {
        Toast.makeText(
            requireContext(),
            "Artista selecionado: $artista",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}