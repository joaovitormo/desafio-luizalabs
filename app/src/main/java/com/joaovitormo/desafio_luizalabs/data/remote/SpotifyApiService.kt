package com.joaovitormo.desafio_luizalabs.data.remote

import com.joaovitormo.desafio_luizalabs.data.model.SpotifyAlbumsResponse
import com.joaovitormo.desafio_luizalabs.data.model.TopArtistsResponse
import com.joaovitormo.desafio_luizalabs.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: String,
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("include_groups") includeGroups: String = "album,single"
    ): Response<SpotifyAlbumsResponse>

}