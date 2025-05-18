package com.joaovitormo.desafio_luizalabs.data.model

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val refresh_token: String?
)