package com.joaovitormo.desafio_luizalabs.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TopArtistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topArtistDao(): TopArtistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "spotify_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
