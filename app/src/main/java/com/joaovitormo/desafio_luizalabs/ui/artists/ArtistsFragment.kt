package com.joaovitormo.desafio_luizalabs.ui.artists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.databinding.FragmentArtistsBinding


class ArtistsFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtistsViewModel by viewModels()

    private val adapter = ArtistsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recyclerViewArtists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewArtists.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ArtistsLoadStateAdapter { adapter.retry() },
            footer = ArtistsLoadStateAdapter { adapter.retry() }
        )

        viewModel.profileImageFile.observe(viewLifecycleOwner) { file ->
            file?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(binding.imageProfile)
            }
        }


        viewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        adapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error

            errorState?.let {
                Log.e("ArtistsFragment", "Erro no carregamento: ${it.error.message}")
                Toast.makeText(requireContext(), "Erro: ${it.error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}