package com.perlovka.mastermindgame.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.GameFragmentBinding
import com.perlovka.mastermindgame.model.Guess
import com.perlovka.mastermindgame.model.UserPreference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "Called ViewModelProvider")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the GameViewModel
        binding.gameViewModel = viewModel

        binding.submitButton.setOnClickListener {
            viewModel.checkGuess()
        }

        // Observe changes to Guess number and display in text views
        viewModel.currentGuessNumber.observe(viewLifecycleOwner, Observer {
            if (it.isBlank()) {
                binding.input0.text = ""
                binding.input1.text = ""
                binding.input2.text = ""
                binding.input3.text = ""
            }
            for (i in it.indices) {
                when (i) {
                    0 -> binding.input0.text = it[0].toString()
                    1 -> binding.input1.text = it[1].toString()
                    2 -> binding.input2.text = it[2].toString()
                    3 -> binding.input3.text = it[3].toString()
                }
            }
        })
        //Create Adapter for Recycle view
        val adapter = GuessAnswerAdapter()

        // Observe changes to list of Guesses and update Recycle View list
        viewModel.guessList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.quessList.adapter = adapter

        // Sets up event listening to navigate the player when the game is finished
        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzEvent ->
            if (buzzEvent != BuzzType.NO_BUZZ) {
                buzz(buzzEvent.pattern)
                viewModel.onBuzzComplete()
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
                else -> showSnackBar(R.string.done_loading_message)
            }
        })


        return binding.root
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameDestinationToResultFragment(
            viewModel.attempts.value ?: 0, viewModel.result.value ?: "", viewModel.secretNumber
        )
        NavHostFragment.findNavController(this).navigate(action)
    }

    /**
     * Called to show snackBar
     */
    private fun showSnackBar(resId: Int) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(resId),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Given a pattern, this method makes sure the device vibrates
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = context?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}