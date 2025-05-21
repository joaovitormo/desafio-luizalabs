package com.joaovitormo.desafio_luizalabs.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joaovitormo.desafio_luizalabs.data.local.entity.PlaylistsRemoteKeys

@Dao
interface PlaylistsRemoteKeysDao {
    @Query("DELETE FROM playlists_remote_keys WHERE playlistId = :playlistId")
    suspend fun clearRemoteKeysByPlaylist(playlistId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PlaylistsRemoteKeys>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeys: PlaylistsRemoteKeys)


    @Query("SELECT * FROM playlists_remote_keys WHERE playlistId = :playlistId")
    suspend fun remoteKeysByPlaylist(playlistId: String): PlaylistsRemoteKeys?


}
