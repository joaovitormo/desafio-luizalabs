package com.joaovitormo.desafio_luizalabs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums_remote_keys")
data class AlbumsRemoteKeys(
    @PrimaryKey val albumId: String,
    val prevKey: Int?,
    val nextKey: Int?,
    val artistId: String
)
