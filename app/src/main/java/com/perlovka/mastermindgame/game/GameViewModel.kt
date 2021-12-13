package com.perlovka.mastermindgame.game

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import com.perlovka.mastermindgame.SecretNumberApi
import com.perlovka.mastermindgame.convertResultToMessage
import com.perlovka.mastermindgame.model.Guess
import com.perlovka.mastermindgame.resultMessage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
enum class SecretNumberApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [GameFragment].
 */
class GameViewModel : ViewModel() {
    // The current attempt
    private val _attempts = MutableLiveData<Int>()
    val attempts: LiveData<Int>
        get() = _attempts

    // The current guess number
    private val _currentGuessNumber = MutableLiveData<String>()
    val currentGuessNumber: LiveData<String>
        get() = _currentGuessNumber


    private val _currentGuess = MutableLiveData<Guess>()
    val currentGuess: LiveData<Guess>
        get() = _currentGuess


    // The LiveData that stores the status of the most recent request
    private val _status = MutableLiveData<SecretNumberApiStatus>()
    val status: LiveData<SecretNumberApiStatus>
        get() = _status

    // Variable to store random number retrieved from network request
    lateinit var secretNumber: String

    // Variable to store result of GuessCheck function
    lateinit var result : String

    // The LiveData that stores the status of the game
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    // List of guesses
    private var guessItemList = mutableListOf<Guess>()
    private val _guessList = MutableLiveData<List<Guess>>()
    val guessList: LiveData<List<Guess>>
        get() = _guessList

    // The LiveData that stores the visibility parameter of Submit Button
    private val _submitButtonClickable = MutableLiveData<Boolean>()
    val submitButtonClickable: LiveData<Boolean>
        get() = _submitButtonClickable

    init {
        _attempts.value = 10
        _currentGuess.value = Guess()
        _currentGuessNumber.value = Guess().number
        _eventGameFinished.value = false
        _submitButtonClickable.value = false

        //Call to getSecretNumber() network request to inflate secretNumber
        getSecretNumber()

        Log.i("GameViewModel", "GameViewModel created")
    }

    // Function to add a new guess to the guesses list
    private fun addGuessToAnswerList(guess: Guess?) {
        guess?.let {
            guessItemList.add(it)
            _guessList.value = guessItemList
        }
    }

    //Function to connect to Internet and inflate secret random number
    private fun getSecretNumber() {
        viewModelScope.launch {
            _status.value = SecretNumberApiStatus.LOADING
            try {

                secretNumber =  SecretNumberApi.retrofitService.getNumber(4, 0, 7, 1, 10, "plain", "new")
                    .filter { it.isDigit() }
                _status.value = SecretNumberApiStatus.DONE
                        Log.i("GameViewModel", "Request done")

        } catch (e: Exception) {
                secretNumber = "1234"
                _status.value = SecretNumberApiStatus.ERROR
                Log.i("GameViewModel", e.message?:"Exception occur")
        }
    }
    }
    //Function to change game status
    fun onGameFinishedComplete() {
        _eventGameFinished.value = false
    }

    /**
     * Executes when the SUBMIT button is clicked.
     * Check if guess matches secret number
     */
    fun checkGuess() {

        var guessMatch = 0
        var appearence = 0

        val guess = _currentGuess.value

        // Check appearance and matches if guess is not null
        guess?.let {
            for (i in it.number.indices) {
                val n = it.number[i]
                if (secretNumber.contains(n)) {
                    if (secretNumber[i] == n) {
                        guessMatch = guessMatch.plus(1)
                    } else {
                        appearence = appearence.plus(1)
                    }
                }
            }
            it.message = convertResultToMessage(guessMatch, appearence)
            _currentGuessNumber.value = it.number
            _submitButtonClickable.value = false

            // Added Current guess to history of guesses list
            addGuessToAnswerList(it)
        }
        // Decrease number of Attempts left
        _attempts.value = (_attempts.value)?.minus(1)

        // Check if number of attempts ends or secret number is guessed
        if (_attempts.value == 0 || guessMatch == 4) {
            result = resultMessage(guessMatch)
            _eventGameFinished.value = true
        }
        // Create new guess object
        val newGuess = Guess()
        _currentGuessNumber.value = newGuess.number
        _currentGuess.value = newGuess
    }

    /**
     * Executes when 0 -7 number button is clicked.
     */
    fun numberSelected(number: Int) {
        val guess = _currentGuess.value
        guess?.let{
            val builder = StringBuilder(it.number)
            when (it.letters) {
                in 0..2 -> it.number = builder.append(number).toString()
                3 -> {
                    it.number = builder.append(number).toString()
                    _submitButtonClickable.value = true
                }
            }
            _currentGuessNumber.value = it.number
            it.letters = it.letters.plus(1)
        }
    }
    /**
     * Executes when the Delete button is clicked.
     */
    fun reset() {
        val guess = _currentGuess.value
        val newGuess = Guess()
        guess?.let{
            it.number = newGuess.number
            it.letters = newGuess.letters
            _currentGuessNumber.value = it.number
            _currentGuess.value = Guess()
        }
    }
/**
 * Called when the ViewModel is dismantled.
 **/
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }

}