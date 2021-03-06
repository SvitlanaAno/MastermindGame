package com.perlovka.mastermindgame.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.TitleFragmentBinding

class TitleFragment: Fragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.title_fragment, container, false)

        binding.playButton.setOnClickListener {
         findNavController().navigate(TitleFragmentDirections.actionTitleToGame())
        }

        binding.ruleButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleDestinationToRulesFlagment())
        }
        return binding.root
    }
}