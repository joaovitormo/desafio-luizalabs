package com.joaovitormo.desafio_luizalabs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlbumsRemoteKeysDao {
    @Query("DELETE FROM albums_remote_keys WHERE artistId = :artistId")
    suspend fun clearRemoteKeysByArtist(artistId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<AlbumsRemoteKeys>)


    @Query("SELECT * FROM albums_remote_keys WHERE artistId = :artistId")
    suspend fun remoteKeysByArtist(artistId: String): AlbumsRemoteKeys?

}
