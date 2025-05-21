package com.joaovitormo.desafio_luizalabs.ui.artistDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.databinding.FragmentArtistDetailBinding


class ArtistDetailFragment : Fragment() {

    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = arguments?.getString("artistName") ?: ""
        val image = arguments?.getString("artistImage")

        adapter = AlbumsAdapter()
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAlbums.adapter = adapter.withLoadStateHeaderAndFooter(
            header = AlbumsLoadStateAdapter {  adapter.retry() },
            footer = AlbumsLoadStateAdapter { adapter.retry() }
        )

        val artistId = arguments?.getString("artistId") ?: ""

        val viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.AndroidViewModelFactory(requireActivity().application) {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArtistDetailViewModel(requireActivity().application, artistId) as T
                }
            }
        )[ArtistDetailViewModel::class.java]

        binding.textViewArtistName.text = name
        Glide.with(this).load(image).into(binding.imageview)



        viewModel.albumsPagingData.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
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
        /*
        adapter.addLoadStateListener { loadState ->
            binding.swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


