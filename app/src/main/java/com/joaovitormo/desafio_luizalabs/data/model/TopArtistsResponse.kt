package com.joaovitormo.desafio_luizalabs.data.model

data class TopArtistsResponse(
    val items: List<ArtistItem>,
)

data class ArtistItem(
    val id: String,
    val name: String,
    val images: List<ImageItem>
)

data class ImageItem(
    val url: String,
    val height: Int,
    val width: Int
)
