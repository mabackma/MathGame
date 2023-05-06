package com.example.mathgame.screens.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentResultBinding
import com.example.mathgame.screens.database.ResultDatabase
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentResultBinding>(inflater,
            R.layout.fragment_result, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ResultDatabase.getInstance(application).resultDatabaseDao
        val viewModelFactory = ResultViewModelFactory(dataSource, application)
        val resultViewModel = ViewModelProvider(this, viewModelFactory).get(ResultViewModel::class.java)
        binding.resultViewModel = resultViewModel
        binding.lifecycleOwner = this

        // Tarkkaillaan allResults livedatan muutoksia ja päivitetään text view.
        resultViewModel.allResults.observe(viewLifecycleOwner, Observer { results ->
            binding.displayResults.text = resultViewModel.makeStringFromResults(results)
        })

        // Tyhjentää tallennetut tulokset
        binding.clearButton.setOnClickListener {
            resultViewModel.onClear()
            Snackbar.make(binding.root, "Tulokset tyhjennetty!", Snackbar.LENGTH_SHORT).show()
        }

        // Menee takaisin alkuun.
        binding.gameButton.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_startFragment)
        }
        return binding.root
    }

}