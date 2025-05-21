package com.joaovitormo.desafio_luizalabs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val ownerName: String,
    val imageUrl: String?
)
