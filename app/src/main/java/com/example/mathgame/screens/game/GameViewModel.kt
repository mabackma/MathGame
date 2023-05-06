package com.example.mathgame.screens.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mathgame.screens.database.Result
import com.example.mathgame.screens.database.ResultDatabase
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _questionsAnswered = MutableLiveData<Int>()
    val questionsAnswered : LiveData<Int>
        get() = _questionsAnswered

    private val _randomNumber1 = MutableLiveData<Int>()
    val randomNumber1 : LiveData<Int>
        get() = _randomNumber1

    private val _randomNumber2 = MutableLiveData<Int>()
    val randomNumber2 : LiveData<Int>
        get() = _randomNumber2

    private val _seconds = MutableLiveData<Int>()
    val seconds : LiveData<Int>
        get() = _seconds

    private var _questionsRight: Int = 0
    private var _userAnswer: Double? = null
    private var _rightAnswer: Double? = null
    private var _operation: String = ""

    init {
        _questionsAnswered.value = 1
        createRandoms()
        _seconds.value = 0
        Timber.i("GameViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed")
    }

    fun setValues(args: GameFragmentArgs) {
        _operation = args.operation
    }

    fun setAnswer(answer: Double) {
        _userAnswer = answer
    }

    fun addSeconds() {
        _seconds.value = _seconds.value!!.plus(1)
    }

    // Palauttaa String muuttujan jossa lukee tehtävä.
    fun showProblem(): String {
        return when(_operation) {
            "+" -> _randomNumber1.value.toString() + " + " + _randomNumber2.value.toString()
            "-" -> _randomNumber1.value.toString() + " - " + _randomNumber2.value.toString()
            "*" -> _randomNumber1.value.toString() + " * " + _randomNumber2.value.toString()
            "/" -> _randomNumber1.value.toString() + " / " + _randomNumber2.value.toString()
            else -> ""
        }
    }

    // Palauttaa montako tehtävää on ratkaistu.
    fun displayAnswered(): String {
        return "Tehtävä: " + _questionsAnswered.value.toString() + "/4"
    }

    // Laskee tehtävän vastauksen Doublena.
    fun getAnswer() {
        _rightAnswer = when(_operation) {
            "+" -> _randomNumber1.value!!.toDouble() + _randomNumber2.value!!.toDouble()
            "-" -> _randomNumber1.value!!.toDouble() - _randomNumber2.value!!.toDouble()
            "*" -> _randomNumber1.value!!.toDouble() * _randomNumber2.value!!.toDouble()
            "/" -> (String.format("%.2f", (_randomNumber1.value!!.toDouble() / _randomNumber2.value!!.toDouble()))).replace(",", ".").toDouble()
            else -> 0.0
        }
        _questionsAnswered.value = (_questionsAnswered.value)?.plus(1)
    }

    // Luo satunnaislukuja.
    fun createRandoms() {
        _randomNumber1.postValue(Random.nextInt(1, 100))
        _randomNumber2.postValue(Random.nextInt(1, 100))
    }

    // Tarkistaa vastauksen.
    fun checkAnswer() {
        if (_userAnswer == _rightAnswer) {
            _questionsRight++
        }
        Timber.i("User: " + _userAnswer.toString() + " Right answer: " + _rightAnswer.toString())
        _userAnswer = null
    }

    fun continueGame(): Boolean {
        if ((_questionsAnswered?.value ?: 0) <= 4) {
            return true
        }
        return false
    }

    fun howManyRight(): Int {
        return _questionsRight
    }

    internal fun insertResult(application: Application, result: Result) {
        val database = ResultDatabase.getInstance(application).resultDatabaseDao
        uiScope.launch {
            Timber.i(result.resultId.toString() + " Operation: " + result.operation.toString() + " Score: " + result.totalScore.toString() + " Time: " + result.totalSeconds.toString() + "s. Date: " + result.date.toString())
            database.insert(result)
        }
    }

}