package com.joaovitormo.desafio_luizalabs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<TopArtistEntity>)

    @Query("SELECT * FROM top_artists")
    suspend fun getAll(): List<TopArtistEntity>
}