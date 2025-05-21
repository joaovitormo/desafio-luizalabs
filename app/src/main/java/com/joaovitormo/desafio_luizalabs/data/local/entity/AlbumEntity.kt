package com.joaovitormo.desafio_luizalabs.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val artistId: String,
    val releaseDate: String
)
