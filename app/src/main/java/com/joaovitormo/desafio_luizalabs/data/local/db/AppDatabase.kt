package com.joaovitormo.desafio_luizalabs.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joaovitormo.desafio_luizalabs.data.local.entity.AlbumEntity
import com.joaovitormo.desafio_luizalabs.data.local.entity.AlbumsRemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.entity.PlaylistEntity
import com.joaovitormo.desafio_luizalabs.data.local.entity.PlaylistsRemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.entity.RemoteKeys
import com.joaovitormo.desafio_luizalabs.data.local.entity.TopArtistEntity
import com.joaovitormo.desafio_luizalabs.data.local.dao.AlbumsDao
import com.joaovitormo.desafio_luizalabs.data.local.dao.AlbumsRemoteKeysDao
import com.joaovitormo.desafio_luizalabs.data.local.dao.PlaylistsDao
import com.joaovitormo.desafio_luizalabs.data.local.dao.PlaylistsRemoteKeysDao
import com.joaovitormo.desafio_luizalabs.data.local.dao.RemoteKeysDao
import com.joaovitormo.desafio_luizalabs.data.local.dao.TopArtistDao

@Database(entities = [TopArtistEntity::class, RemoteKeys::class, AlbumEntity::class, AlbumsRemoteKeys::class, PlaylistEntity::class, PlaylistsRemoteKeys::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topArtistDao(): TopArtistDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun albumsRemoteKeysDao(): AlbumsRemoteKeysDao
    abstract fun albumsDao(): AlbumsDao
    abstract fun playlistRemoteKeysDao(): PlaylistsRemoteKeysDao
    abstract fun playlistsDao(): PlaylistsDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spotify_db"
                )
                    .fallbackToDestructiveMigration() // cuidado, destrói dados em upgrade, só usar no dev
                    .build().also { INSTANCE = it }
            }
        }
    }
}

