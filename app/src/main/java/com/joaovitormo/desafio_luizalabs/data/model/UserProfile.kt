package com.joaovitormo.desafio_luizalabs.data.model

data class UserProfile(
    val display_name: String?,
    val external_urls: ExternalUrls?,
    val followers: Followers?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val type: String?,
    val uri: String?
)

data class ExternalUrls(
    val spotify: String?
)

data class Followers(
    val href: String?,
    val total: Int?
)

data class Image(
    val height: Int?,
    val url: String?,
    val width: Int?
)
