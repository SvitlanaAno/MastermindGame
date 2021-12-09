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

        binding.guess = Guess()
        val adapter = GuessAnswerAdapter()
        //Create Adapter for Recycle view
        binding.quessList.adapter = adapter

        binding.submitButton.setOnClickListener {
            viewModel.checkGuess(binding.guess)
            binding.guess = Guess()
        }

        viewModel.currentGuessNumber.observe(viewLifecycleOwner, Observer {
            binding.editTextNumber.text = it
        })
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

}