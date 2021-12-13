package com.perlovka.mastermindgame.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.ResultFragmentBinding
import com.perlovka.mastermindgame.model.UserPreference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ResultFragment: Fragment() {
    private lateinit var viewModel : ResultViewModel
    private lateinit var viewModelFactory: ResultViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view and obtain an instance of the binding class.
        val binding: ResultFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.result_fragment,
            container,
            false
        )

        //Get a reference to the application context.
        val application = requireNotNull(this.activity).application

        // Get args using by navArgs property delegate
        val resultFragmentArgs by navArgs<ResultFragmentArgs>()

        // Create viewModel with viewModelFactory
        viewModelFactory = ResultViewModelFactory(resultFragmentArgs.score, resultFragmentArgs.message, resultFragmentArgs.secretNumber, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ResultViewModel::class.java)

        binding.resultViewModel = viewModel
        binding.lifecycleOwner = this

        // Observe changes to eventPlayAgain status and navigate to GameFragment
        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { value->
            if(value){
                findNavController().navigate(ResultFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })
        return binding.root
    }
}