package com.example.mathgame.screens.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 4, exportSchema = false)
abstract class ResultDatabase : RoomDatabase() {

    abstract val resultDatabaseDao: ResultDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ResultDatabase? = null

        fun getInstance(context: Context) : ResultDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ResultDatabase::class.java,
                        "result_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

