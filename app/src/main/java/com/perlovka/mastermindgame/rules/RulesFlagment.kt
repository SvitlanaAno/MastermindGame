package com.perlovka.mastermindgame.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.RulesFragmentBinding

class RulesFlagment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

// Inflate the layout for this fragment
        val binding: RulesFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.rules_fragment, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigate(RulesFlagmentDirections.actionRulesDestinationToTitleDestination())
        }
        return binding.root
    }
}