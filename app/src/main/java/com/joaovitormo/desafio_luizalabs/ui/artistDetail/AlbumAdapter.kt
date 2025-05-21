package com.joaovitormo.desafio_luizalabs.ui.artistDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.data.local.AlbumEntity
import com.joaovitormo.desafio_luizalabs.databinding.ItemAlbumBinding

class AlbumsAdapter : PagingDataAdapter<AlbumEntity, AlbumsAdapter.AlbumViewHolder>(ALBUM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        if (album != null) holder.bind(album)
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumEntity) {
            binding.textAlbumName.text = album.name
            binding.releaseDateTextView.text = album.releaseDate
            Glide.with(binding.imageAlbum.context).load(album.imageUrl).into(binding.imageAlbum)
        }
    }

    companion object {
        private val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<AlbumEntity>() {
            override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity) = oldItem == newItem
        }
    }
}
