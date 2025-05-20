package com.joaovitormo.desafio_luizalabs.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joaovitormo.desafio_luizalabs.databinding.ItemArtistBinding
import com.joaovitormo.desafio_luizalabs.data.local.TopArtistEntity


class ArtistsAdapter(
    private var artists: List<TopArtistEntity>,
    private val onClick: (TopArtistEntity) -> Unit
) : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    inner class ArtistViewHolder(val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: TopArtistEntity) {
            binding.textArtistName.text = artist.name
            Glide.with(binding.root.context)
                .load(artist.imagePath)
                .into(binding.imageview)
            itemView.setOnClickListener { onClick(artist) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun getItemCount() = artists.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    fun updateData(newArtists: List<TopArtistEntity>) {
        artists = newArtists
        notifyDataSetChanged()
    }
}
