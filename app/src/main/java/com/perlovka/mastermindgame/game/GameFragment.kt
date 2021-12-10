package com.perlovka.mastermindgame.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.GameFragmentBinding
import com.perlovka.mastermindgame.model.Guess

class GameFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    /**
     * Called when the Fragment is ready to display content to the screen.
     * This function uses DataBindingUtil to inflate R.layout.game_fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "Called ViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the GameViewModel
        binding.gameViewModel = viewModel

        //binding.guess = Guess()
        val adapter = GuessAnswerAdapter()

        //Create Adapter for Recycle view
        binding.quessList.adapter = adapter

        binding.submitButton.setOnClickListener {
            viewModel.checkGuess()
            //binding.guess = Guess()
        }

        viewModel.currentGuessNumber.observe(viewLifecycleOwner, Observer {
          // NEED TO REWRITE mayby loop
            when(it.length){
                0 -> {  binding.input0.text =""
                    binding.input1.text =""
                    binding.input2.text =""
                    binding.input3.text =""
                }
                1 -> binding.input0.text = it.get(0).toString()
                2 -> binding.input1.text = it.get(1).toString()
                3 -> binding.input2.text = it.get(2).toString()
                4 -> binding.input3.text = it.get(3).toString()
            }
        })

        // Observe changes to list of Guesses and update Recycle View list
        viewModel.guessList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer { value ->
            if (value) {
                gameFinished()
                viewModel.onGameFinishedComplete()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                SecretNumberApiStatus.ERROR -> {
                    showSnackBar(R.string.no_internet_message)
                }
                SecretNumberApiStatus.DONE -> {
                    showSnackBar(R.string.done_loading_message)
                }
            }
        })
        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameDestinationToResultFragment(
            viewModel.attempts.value ?: 0, viewModel.result
        )
        NavHostFragment.findNavController(this).navigate(action)
    }
    /**
     * Called to show snackBar
     */
    private fun showSnackBar(resId :Int){
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(resId),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}