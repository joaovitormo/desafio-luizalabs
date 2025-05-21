package com.joaovitormo.desafio_luizalabs.data.remote.api

import com.joaovitormo.desafio_luizalabs.data.model.TokenResponse
import retrofit2.http.*
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyAuthApi {

    @FormUrlEncoded
    @POST("api/token")
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code_verifier") codeVerifier: String
    ): Response<TokenResponse>
}