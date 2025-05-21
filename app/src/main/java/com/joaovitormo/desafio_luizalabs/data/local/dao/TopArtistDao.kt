package com.joaovitormo.desafio_luizalabs.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joaovitormo.desafio_luizalabs.data.local.entity.TopArtistEntity

@Dao
interface TopArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<TopArtistEntity>)

    @Query("SELECT count(*) FROM top_artists")
    suspend fun getItemCount(): Int

    @Query("SELECT * FROM top_artists")
    fun getPagedArtists(): PagingSource<Int, TopArtistEntity>

    @Query("DELETE FROM top_artists")
    suspend fun clearAll()
}