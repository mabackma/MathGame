package com.example.mathgame.screens.score

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mathgame.screens.database.Result
import com.example.mathgame.screens.database.ResultDatabase
import kotlinx.coroutines.*
import timber.log.Timber

class ScoreViewModel : ViewModel() {

    init {
        Timber.i("ScoreViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("ScoreViewModel destroyed")
    }

    // Palauttaa moneenko kysymykseen vastattiin oikein.
    internal suspend fun displayAnswers(application: Application): String {
        var latestScore = getLatest(application)
        return latestScore.totalScore.toString() + "/4 Oikein"
    }

    // Palauttaa kuinka kauan tehtävien ratkominen kesti.
    internal suspend fun displayTime(application: Application): String {
        var latestTime = getLatest(application)
        return "Käytit: " + latestTime.totalSeconds.toString() + "s"
    }


    // Palauttaa viimeisimmän tuloksen tietokannasta.
    internal suspend fun getLatest(application: Application): Result {
        val database = ResultDatabase.getInstance(application).resultDatabaseDao
        val latestResult: Result
        withContext(Dispatchers.IO) {
            latestResult = database.getLatest() ?: Result(operation = "", totalScore = 0, totalSeconds = 0, date = "")
        }
        return latestResult
    }

}