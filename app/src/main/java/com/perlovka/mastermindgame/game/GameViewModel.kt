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

class GameViewModel : ViewModel() {
    // The current attempt
    private val _attempts = MutableLiveData<Int>()
    val attempts: LiveData<Int>
        get() = _attempts

    // The current guess
    private val _currentGuessNumber = MutableLiveData<String>()
    val currentGuessNumber: LiveData<String>
        get() = _currentGuessNumber

    //NEED TO REWRITE
    private var secretNumber = hashMapOf('1' to 0, '2' to 1, '3' to 2, '4' to 3)

    var result = ""
    private val _eventGameFinished = MutableLiveData<Boolean>()

    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    // List of guesses
    private var guessItemList = mutableListOf<Guess>()


    private val _guessList = MutableLiveData<List<Guess>>()

    val guessList: LiveData<List<Guess>>
        get() = _guessList

    private val _submitButtonClickable = MutableLiveData<Boolean>()
    val submitButtonClickable: LiveData<Boolean>
        get() = _submitButtonClickable


    // Add a new guess to the guesses list
    fun addGuessToAnswerList(guess: Guess?) {
        if (guess != null) {
            guessItemList.add(guess)
            _guessList.value = guessItemList
        }
    }

    init {
        _attempts.value = 10
        _currentGuessNumber.value = Guess().number
        _eventGameFinished.value = false
        _submitButtonClickable.value = false
        //Call to getSecretNumber() networt request to inflate secretNumber
        getSecretNumber()
        Log.i("GameViewModel", "GameViewModel created")
    }

    //NEED TO REWRITE
    private fun getSecretNumber() {
        viewModelScope.launch {
            try {
            SecretNumberApi.retrofitService.getNumber(4, 0, 7, 1, 10, "plain", "new")
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.i("GameViewModel", "Request fail")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        var snumner = response.body()?.filter { it.isDigit() } ?: "1111"
                        secretNumber = hashMapOf(
                            snumner.get(0) to 0,
                            snumner.get(1) to 1,
                            snumner.get(2) to 2,
                            snumner.get(3) to 3
                        )
                        Log.i("GameViewModel", "Request done")
                    }
                })
        } catch (e: Exception) {
                Log.i("GameViewModel", e.message?:"Exception occur")
                return@launch
        }
    }
        }

    fun onGameFinishedComplete() {
        _eventGameFinished.value = false
    }

    fun checkGuess(guess: Guess?) {
        var guessMatch = 0
        var appearence = 0

        //Check appearance and matches if guess is not null
        guess?.let {
            //         guess.number = guess.number.replace("\\s".toRegex(), "")
            val number = guess.number
            for (i in number.indices) {
                var n = number.get(i)
                if (secretNumber.containsKey(n)) {
                    if (secretNumber.get(n) == i) {
                        guessMatch = guessMatch.plus(1)
                    } else {
                        appearence = appearence.plus(1)
                    }
                }
            }
            guess.message = convertResultToMessage(guessMatch, appearence)
            _currentGuessNumber.value = guess.number
        }
        addGuessToAnswerList(guess)
        _submitButtonClickable.value = false

        _attempts.value = (_attempts.value)?.minus(1)

        if (_attempts.value == 0 || guessMatch == 4) {
            result = resultMessage(guessMatch)
            _eventGameFinished.value = true
        }
    }

    //NEED TO REWRITE
    fun numberSelected(number: Int, guess: Guess) {
        // Need to rewrite
        guess.number = guess.number.filter { it.isDigit() }
        val builder = StringBuilder(guess.number)
        when (guess.letters) {
            0 -> guess.number = builder.append(number).append(" _ _ _").toString()
            1 -> guess.number = builder.append(number).append(" _ _").toString()
            2 -> guess.number = builder.append(number).append(" _").toString()
            3 -> {
                guess.number = builder.append(number).toString()
                _submitButtonClickable.value = true
            }
        }
        _currentGuessNumber.value = guess.number
        guess.letters = guess.letters.plus(1)
    }

    fun reset(guess: Guess) {
        val newGuess = Guess()
        guess.number = newGuess.number
        guess.letters = newGuess.letters
        _currentGuessNumber.value = guess.number
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }

}