package com.joaovitormo.desafio_luizalabs.data.model

data class CreatePlaylistBody(
    val name: String,
    val description: String = "Criada via app",
    val public: Boolean = false
)