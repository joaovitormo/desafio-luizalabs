package com.joaovitormo.desafio_luizalabs.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joaovitormo.desafio_luizalabs.data.local.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<PlaylistEntity>)

    @Query("SELECT count(*) FROM playlists")
    suspend fun getItemCount(): Int

    @Query("SELECT * FROM playlists")
    fun getPagedPlaylists(): PagingSource<Int, PlaylistEntity>

    @Query("DELETE FROM playlists")
    suspend fun clearAll()
}
