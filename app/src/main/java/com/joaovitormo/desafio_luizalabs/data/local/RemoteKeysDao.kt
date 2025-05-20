package com.joaovitormo.desafio_luizalabs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysById(id: String): RemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeys: RemoteKeys)

    @Query("DELETE FROM remote_keys WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeyById(id: String): RemoteKeys?
}
