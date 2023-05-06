package com.example.mathgame.screens.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    // Laskutoimituksen tyyppi joka lähetetään seuraavalle fragmentille
    var operation: String = "+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentStartBinding>(inflater,
            R.layout.fragment_start, container, false)

        // Valitsee merkin laskutoimituksen tyypin perusteella
        binding.radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.additionButton -> {
                    operation = "+"
                }
                R.id.subtractionButton -> {
                    operation = "-"
                }
                R.id.multiplicationButton -> {
                    operation = "*"
                }
                R.id.divisionButton -> {
                    operation = "/"
                }
            }
        }

        // Lähetetään laskutoimituksen tyyppi GameFragment fragmentille
        binding.startButton.setOnClickListener {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToGameFragment(
                    operation
                )
            )
        }
        return binding.root
    }

}