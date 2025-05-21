package com.joaovitormo.desafio_luizalabs.data.remote

import com.joaovitormo.desafio_luizalabs.data.model.CreatePlaylistBody
import com.joaovitormo.desafio_luizalabs.data.model.PlaylistResponse
import com.joaovitormo.desafio_luizalabs.data.model.PlaylistsResponse
import com.joaovitormo.desafio_luizalabs.data.model.SpotifyAlbumsResponse
import com.joaovitormo.desafio_luizalabs.data.model.TopArtistsResponse
import com.joaovitormo.desafio_luizalabs.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @GET("me/playlists")
    suspend fun getPlaylists(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PlaylistsResponse>

    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Header("Authorization") token: String,
        @Path("user_id") userId: String,
        @Body body: CreatePlaylistBody
    ): PlaylistResponse

}