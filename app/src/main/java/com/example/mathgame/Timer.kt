package com.example.mathgame

import android.os.Handler
import androidx.lifecycle.*
import com.example.mathgame.screens.game.GameViewModel

class Timer(lifecycle: Lifecycle, model: GameViewModel) : LifecycleObserver {

    private var handler = Handler()
    private lateinit var runnable: Runnable
    var isPaused = false
    private lateinit var timerData: GameViewModel

    init {
        lifecycle.addObserver(this)
        this.timerData = model
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {

        runnable = Runnable {
            if (!isPaused) {
                timerData.addSeconds()
            }

            handler.postDelayed(runnable, 1000)
        }

        // Käynnistetään ajastin ensimmäisen kerran
        handler.postDelayed(runnable, 1000)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {
        handler.removeCallbacks(runnable)
    }

}