package com.joaovitormo.desafio_luizalabs.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlbumsDao {

    @Query("DELETE FROM albums WHERE artistId = :artistId")
    suspend fun clearAlbumsByArtist(artistId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("SELECT * FROM albums WHERE artistId = :artistId")
    fun getAlbumsByArtistId(artistId: String): PagingSource<Int, AlbumEntity>



}