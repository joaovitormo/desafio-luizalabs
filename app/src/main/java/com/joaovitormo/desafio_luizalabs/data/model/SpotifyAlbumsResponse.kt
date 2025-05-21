package com.joaovitormo.desafio_luizalabs.data.model

data class SpotifyAlbumsResponse(
    val items: List<AlbumItem>,
    val total: Int,
    val limit: Int,
    val offset: Int
)

data class AlbumItem(
    val id: String,
    val name: String,
    val images: List<AlbumImage>,
    val release_date : String
)

data class AlbumImage(
    val url: String,
    val height: Int,
    val width: Int
)
