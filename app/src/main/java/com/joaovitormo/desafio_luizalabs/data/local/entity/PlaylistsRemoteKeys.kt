package com.joaovitormo.desafio_luizalabs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "playlists_remote_keys")
data class PlaylistsRemoteKeys(
    @PrimaryKey val playlistId: String,
    val prevKey: Int?,
    val nextKey: Int?
)