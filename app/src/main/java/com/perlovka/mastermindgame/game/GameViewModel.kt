package com.perlovka.mastermindgame.game

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import com.perlovka.mastermindgame.convertResultToMessage
import com.perlovka.mastermindgame.model.Guess
import com.perlovka.mastermindgame.resultMessage

class GameViewModel : ViewModel(){
    // The current attempt
    private val _attempts = MutableLiveData<Int>()
    val attempts : LiveData<Int>
        get() = _attempts

    // The current attempt
    private val _currentGuessNumber = MutableLiveData<String>()
    val currentGuessNumber : LiveData<String>
        get() = _currentGuessNumber

    private val SECRET = hashMapOf(3 to 0 , 1 to 1, 5 to 2, 6 to 3)


    var result = ""
    private val _eventGameFinished = MutableLiveData<Boolean>()

    val eventGameFinished : LiveData<Boolean>
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
        Log.i("GameViewModel", "GameViewModel created")
    }

    fun onGameFinishedComplete() {
        _eventGameFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
    }


     fun checkGuess(guess: Guess?) {
        var guessMatch = 0
        var appearence = 0

        //Check appearance and matches if guess is not null
        guess?.let {
   //         guess.number = guess.number.replace("\\s".toRegex(), "")
            val number = guess.number
            for (i in number.indices) {
                var n = number.get(i).digitToInt()
                if(SECRET.containsKey(n)){
                    if(SECRET.get(n) == i) {
                        guessMatch = guessMatch.plus(1)
                    }
                        else{
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

    fun numberSelected(number: Int, guess: Guess){
        // Need to rewrite
        guess.number =   guess.number.filter {it.isDigit()}
        val builder = StringBuilder(guess.number)
        when(guess.letters) {
            0 -> guess.number = builder.append(number).append(" _ _ _").toString()
            1 -> guess.number = builder.append(number).append(" _ _").toString()
            2 -> guess.number = builder.append(number).append(" _").toString()
            3 ->  {
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
}