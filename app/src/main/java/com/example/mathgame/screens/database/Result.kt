package com.example.mathgame.screens.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "results_table")
data class Result (
    @PrimaryKey(autoGenerate = true)
    var resultId: Long = 0L,

    @ColumnInfo(name = "type_of_calculation")
    var operation: String,

    @ColumnInfo(name = "total_score")
    var totalScore: Int,

    @ColumnInfo(name = "total_seconds")
    var totalSeconds: Long,

    @ColumnInfo(name = "date_of_game")
    var date: String
)