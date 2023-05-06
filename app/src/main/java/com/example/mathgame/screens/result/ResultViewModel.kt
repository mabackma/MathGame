package com.example.mathgame.screens.result

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathgame.screens.database.ResultDatabase
import com.example.mathgame.screens.database.ResultDatabaseDao
import com.example.mathgame.screens.database.Result
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import timber.log.Timber


class ResultViewModel(
            val database: ResultDatabaseDao,
            application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope((Dispatchers.Main + viewModelJob))

    // Uusi tulos
    private var newResult = MutableLiveData<Result?>()

    // Haetaan kaikki tulokset ja luodaan niistä String
    internal val allResults: LiveData<List<Result>> = database.getAllResults()
    private var resultsList = listOf<Result>()

    private val _resultsString = MutableLiveData("")
    val resultsString: LiveData<String>
        get() = _resultsString

    init {
        initializeNewResult()

        allResults.observeForever { results ->
            updateResultsString()
        }

        Timber.i("ResultViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Timber.i("ResultViewModel destroyed")
    }

    private fun initializeNewResult() {
        uiScope.launch {
            newResult.value = getNewResultFromDatabase()
        }
    }

    private suspend fun getNewResultFromDatabase(): Result? {
        return withContext(Dispatchers.IO) {
            val latestResult = database.getLatest()
            latestResult
        }
    }

    private suspend fun insert(result: Result) {
        withContext(Dispatchers.IO) {
            database.insert(result)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
        //    newResult = null
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun updateResultsString() {
        val resultsList = allResults.value?.toMutableList() ?: mutableListOf()
        _resultsString.value = makeStringFromResults(resultsList)
    }

    internal fun makeStringFromResults(resultsList: List<Result>): String {
        var returnString: String = ""
        for (result in resultsList) {
            val op: String = when(result.operation) {
                "+" -> "YHTEENLASKU"
                "-" -> "VÄHENNYSLASKU"
                "*" -> "KERTOLASKU"
                "/" -> "JAKOLASKU"
                else -> ""
            }
            returnString +=
                    "${result.date}\n${op}\n" +
                    "Oikein: ${result.totalScore}/4 | Aika: ${result.totalSeconds}s.      \n\n"
        }
        return returnString.toString()
    }
}