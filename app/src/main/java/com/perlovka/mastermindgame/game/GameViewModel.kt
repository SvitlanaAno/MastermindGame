package com.perlovka.mastermindgame.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.perlovka.mastermindgame.model.Guess

class GameViewModel : ViewModel(){
    // The current attempt
    private val _attempts = MutableLiveData<Int>()

    val attempts : LiveData<Int>
        get() = _attempts


    private val SECRET = hashMapOf(3 to 0 , 1 to 1, 5 to 2, 6 to 3)
    private var match = 0
    private var appearence = 0
    
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished


    // List of guesses
    private var guessItemList = mutableListOf<Guess>()

    private val _guessList = MutableLiveData<List<Guess>>()
    val guessList: LiveData<List<Guess>>
        get() = _guessList


    // Add a new guess to the guesses list
    fun addGuessToAnswerList(guess: Guess?) {
        if (guess != null) {
            guessItemList.add(guess)
            _guessList.value = guessItemList
        }
    }



    init {
        _attempts.value = 10
        _eventGameFinished.value = false


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
            var number = guess.number

            for (i in number.indices) {
                var n = number.get(i).digitToInt()
                if(SECRET.containsKey(n)){
                    if(SECRET.get(n) == i) {
                        guessMatch +=1
                    }
                        else{
                        appearence += 1
                    }
            }
            }
            guess.message = convertResultToMessage(guessMatch, appearence)
        }
        addGuessToAnswerList(guess)
        _attempts.value = (_attempts.value)?.minus(1)
    }

    private fun convertResultToMessage(match: Int, appearance: Int): String {
        if(match > 0) return "Guess a correct number and correct location"
        else if(appearance > 0) return "Guess a correct number"
        else return "Guess was incorrect"
    }
}