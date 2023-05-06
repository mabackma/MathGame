package com.example.mathgame.screens.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mathgame.R
import com.example.mathgame.Timer
import com.example.mathgame.databinding.FragmentGameBinding
import com.example.mathgame.screens.database.Result
import com.example.mathgame.screens.database.ResultDatabase
import com.example.mathgame.screens.result.ResultViewModel
import com.example.mathgame.screens.result.ResultViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class GameFragment : Fragment() {

    private lateinit var gameTimer: Timer
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater,
            R.layout.fragment_game, container, false)

        // Luodaan viewModel.
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //Luodaan ajastin.
        gameTimer = Timer(this.lifecycle, gameViewModel)

        // Näytetään aika.
        val timeSpent = binding.root.findViewById<TextView>(R.id.time_text)
        gameViewModel.seconds.observe(viewLifecycleOwner, Observer{newValue ->
            timeSpent.text = newValue.toString() + " s."
        })

        // Haetaan laskutoimitus.
        val args = GameFragmentArgs.fromBundle(requireArguments())
        gameViewModel.setValues(args)

        // Näytetään tehtävä.
        val problem = binding.root.findViewById<TextView>(R.id.problem_text)
        gameViewModel.randomNumber2.observe(viewLifecycleOwner, Observer{newValue ->
            problem.text = gameViewModel.showProblem()
        })

        // Näytetään monesko tehtävä on meneillään.
        val amountAnswered = binding.root.findViewById<TextView>(R.id.header_text)
        gameViewModel.questionsAnswered.observe(viewLifecycleOwner, Observer{newValue ->
            amountAnswered.text = gameViewModel.displayAnswered()
        })

        // Vastataan tehtävään.
        binding.submitButton.setOnClickListener {
            // Lasketaan oikea vastaus.
            gameViewModel.getAnswer()

            // Hyväksytään käyttäjän antama vastaus.
            val answer = binding.answerInput.text.toString()
            if(answer != "") {
                gameViewModel.setAnswer(answer.toDouble())
            }

            // Tyhjennetään input kenttä.
            binding.answerInput.setText("")

            // Lisätään piste jos vastaus oli oikein.
            gameViewModel.checkAnswer()

            // Annetaan seuraava tehtävä jos alle neljään tehtävään on vastattu.
            // Muulloin jatketaan seuraavaan fragmenttiin.
            if (gameViewModel.continueGame()) {
                gameViewModel.createRandoms()
                problem.text = gameViewModel.showProblem()
            }
            else {
                // Päivämäärä string muodossa
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val dateString = dateFormat.format(currentDate)

                // Tallennetaan tulos tietokantaan.
                val application = requireNotNull(this.activity).application
                val result = Result(operation = args.operation, totalScore = gameViewModel.howManyRight(), totalSeconds = gameViewModel.seconds.value!!.toLong(), date = dateString)
                gameViewModel.insertResult(application, result)

                // Siirrytään seuraavaan fragmenttiin.
                findNavController().navigate(R.id.action_gameFragment_to_endFragment)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
        gameTimer.isPaused = false
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
        gameTimer.isPaused = true
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
}