package com.joaovitormo.desafio_luizalabs.ui.playlists

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.databinding.FragmentPlaylistsBinding
import com.joaovitormo.desafio_luizalabs.ui.addPlaylist.AddPlaylistBottomSheet


class PlaylistsFragment : Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by viewModels()

    private lateinit var adapter : PlaylistsAdapter

    var hasObserved = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = PlaylistsAdapter()
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPlaylists.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PlaylistsLoadStateAdapter { adapter.retry() },
            footer = PlaylistsLoadStateAdapter { adapter.retry() }
        )

        binding.addPlaylist.setOnClickListener {
            val bottomSheet = AddPlaylistBottomSheet()
            bottomSheet.onPlaylistCreated = {
                onResume()
            }
            bottomSheet.show(parentFragmentManager, "AddPlaylistBottomSheet")
        }

        viewModel.profileImageFile.observe(viewLifecycleOwner) { file ->
            file?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(binding.imageProfile)
            }
        }


        if (!hasObserved) {
            viewModel.pagingData.observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
            hasObserved = true
        }
        var wasLoading = false

        adapter.addLoadStateListener { loadState ->

            val isLoading = loadState.source.refresh is LoadState.Loading

            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

            if (wasLoading && !isLoading) {
                binding.recyclerViewPlaylists.smoothScrollToPosition(0)
            }

            wasLoading = isLoading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error

            errorState?.let {
                Log.e("PlaylistsFragment", "Erro no carregamento: ${it.error.message}")
                Toast.makeText(requireContext(), "Erro: ${it.error.message}", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }
}

