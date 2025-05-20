package com.joaovitormo.desafio_luizalabs.data.remote

import com.joaovitormo.desafio_luizalabs.data.model.TopArtistsResponse
import com.joaovitormo.desafio_luizalabs.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface SpotifyApiService {

    @GET("me")
    suspend fun getCurrentUserProfile(
        @Header("Authorization") authHeader: String
    ): Response<UserProfile>

    @GET("me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<TopArtistsResponse>
}