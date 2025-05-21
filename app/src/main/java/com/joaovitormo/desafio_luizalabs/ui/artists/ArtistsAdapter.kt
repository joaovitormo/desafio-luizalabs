package com.joaovitormo.desafio_luizalabs.ui.artists

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.databinding.ItemArtistBinding
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistEntity


class ArtistsAdapter(
    private val onItemClick: (TopArtistEntity) -> Unit
) : PagingDataAdapter<TopArtistEntity, ArtistsAdapter.ArtistViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position) ?: return
        holder.bind(artist)
    }

    class ArtistViewHolder(
        private val binding: ItemArtistBinding,
        private val onItemClick: (TopArtistEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: TopArtistEntity) {
            binding.textArtistName.text = artist.name
            Glide.with(binding.imageview.context)
                .load(artist.imagePath)
                .into(binding.imageview)

            binding.root.setOnClickListener {
                onItemClick(artist)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TopArtistEntity>() {
            override fun areItemsTheSame(oldItem: TopArtistEntity, newItem: TopArtistEntity) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: TopArtistEntity, newItem: TopArtistEntity) = oldItem == newItem
        }
    }
}

