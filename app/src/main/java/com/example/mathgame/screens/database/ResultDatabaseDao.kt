package com.example.mathgame.screens.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDatabaseDao {

    @Insert
    suspend fun insert(result: Result)

    @Query("DELETE FROM results_table")
    suspend fun clear()

    @Query("SELECT * FROM results_table ORDER BY total_score DESC, total_seconds ASC")
    fun getAllResults(): LiveData<List<Result>>

    @Query("SELECT * FROM results_table ORDER BY resultId DESC LIMIT 1")
    suspend fun getLatest(): Result?

}
