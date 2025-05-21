package com.joaovitormo.desafio_luizalabs.data.model

data class PlaylistResponse(
    val id: String,
    val name: String,
    val description: String,
    val owner: OwnerPlaylist
)

data class OwnerPlaylist(
    val id: String,
    val display_name: String
)
