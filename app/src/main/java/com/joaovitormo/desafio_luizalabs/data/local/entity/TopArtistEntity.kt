package com.joaovitormo.desafio_luizalabs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_artists")
data class TopArtistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imagePath: String
)
