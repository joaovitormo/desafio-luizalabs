package com.joaovitormo.desafio_luizalabs.ui.addPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joaovitormo.desafio_luizalabs.R
import com.joaovitormo.desafio_luizalabs.databinding.FragmentAddPlaylistBottomSheetListDialogItemBinding
import com.joaovitormo.desafio_luizalabs.ui.playlists.PlaylistsViewModel

class AddPlaylistBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentAddPlaylistBottomSheetListDialogItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistsViewModel by activityViewModels()

    var onPlaylistCreated: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBottomSheetListDialogItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.addNewPlaylist.setOnClickListener {
            val playlistName = binding.editTextPlaylistName.text.toString().trim()

            if (playlistName.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, preencha o nome da playlist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = viewModel.token
            val userId = viewModel.userId

            if (token.isNullOrEmpty() || userId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Token ou ID do usuário não disponível", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createPlaylist(
                userId = userId,
                token = token,
                playlistName = playlistName,
                onSuccess = {
                    Toast.makeText(requireContext(), "Playlist criada com sucesso!", Toast.LENGTH_SHORT).show()
                    onPlaylistCreated?.invoke()
                    dismiss()
                },
                onError = {
                    Toast.makeText(requireContext(), "Erro ao criar playlist: ${it.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }


    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

