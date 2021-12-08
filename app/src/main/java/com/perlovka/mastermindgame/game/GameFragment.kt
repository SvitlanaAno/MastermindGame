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
        binding = DataBindingUtil.inflate( inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "Called ViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel

        binding.guess = Guess()
        val adapter = GuessAnswerAdapter()
      //Create Adapter for Recycle view
        binding.quessList.adapter = adapter
        binding.setLifecycleOwner(this)

        binding.submitButton.setOnClickListener{
            viewModel.checkGuess(binding.guess)
            binding.guess = Guess()
        }

        viewModel.guessList.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
            }
        })
        return binding.root
    }
}