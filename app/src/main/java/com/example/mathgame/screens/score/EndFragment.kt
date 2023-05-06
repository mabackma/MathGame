package com.example.mathgame.screens.score

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
import com.example.mathgame.databinding.FragmentEndBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EndFragment : Fragment() {

    private lateinit var scoreViewModel: ScoreViewModel
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEndBinding>(inflater,
            R.layout.fragment_end, container, false)

        // Luodaan viewModel.
        scoreViewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)

        val application = requireNotNull(this.activity).application

        // Näytetään montako tehtävää meni oikein.
        var total = binding.root.findViewById<TextView>(R.id.header_text)
        uiScope.launch {
            total.text = scoreViewModel.displayAnswers(application)
        }
        // Näytetään aika.
        var time = binding.root.findViewById<TextView>(R.id.problem_text)
        uiScope.launch {
            time.text = scoreViewModel.displayTime(application)
        }


        // Siirrytään seuraavaan fragmenttiin.
        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.action_endFragment_to_resultFragment)
        }
        return binding.root
    }

}