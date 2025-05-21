package com.joaovitormo.desafio_luizalabs.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.R
import com.joaovitormo.desafio_luizalabs.data.local.entity.PlaylistEntity
import com.joaovitormo.desafio_luizalabs.databinding.ItemPlaylistBinding

class PlaylistsAdapter : PagingDataAdapter<PlaylistEntity, PlaylistsAdapter.PlaylistViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = getItem(position)
        if (playlist != null) holder.bind(playlist)

    }

    class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistEntity) {
            binding.textPlaylistName.text = playlist.name
            binding.textPlaylistOwnerName.text = playlist.ownerName

            Glide.with(binding.imageview.context)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.playlist_without_image)
                .error(R.drawable.playlist_without_image)
                .into(binding.imageview)

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaylistEntity>() {
            override fun areItemsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: PlaylistEntity, newItem: PlaylistEntity) = oldItem == newItem
        }
    }
}