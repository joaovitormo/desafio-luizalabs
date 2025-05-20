package com.joaovitormo.desafio_luizalabs.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TopArtistEntity::class, RemoteKeys::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topArtistDao(): TopArtistDao
    abstract fun remoteKeysDao(): RemoteKeysDao

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

