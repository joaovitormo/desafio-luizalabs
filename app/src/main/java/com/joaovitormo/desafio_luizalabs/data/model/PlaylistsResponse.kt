package com.joaovitormo.desafio_luizalabs.data.model

data class PlaylistsResponse(
    val items: List<Playlist>
)

data class Playlist(
    val id: String,
    val name: String,
    val owner: Owner,
    val images: List<PlaylistImage>
)

data class Owner(
    val display_name: String
)

data class PlaylistImage(
    val url: String
)