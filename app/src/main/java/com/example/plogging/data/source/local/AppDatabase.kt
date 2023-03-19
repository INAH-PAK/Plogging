package com.example.plogging.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plogging.data.model.AloneRecode

@Database(entities = [AloneRecode::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun aloneRecordeDao(): AloneRecodeDao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "ploggingapp-db"
            ).build().also {
                instance = it
            }
        }
    }
}