package com.perlovka.mastermindgame.result

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.perlovka.mastermindgame.model.UserPreference
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResultViewModel(attempts : Int, message: String, secretNumber: String, application: Application) : AndroidViewModel(application) {

    // Get a reference to the UsrPreference
    private var userPreference: UserPreference = UserPreference(application)

    private val currentTotal = attempts*5
    var previousTotal =  0

    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _totalGameScore = MutableLiveData<Int>()
    val totalGameScore : LiveData<Int>
        get() = _totalGameScore

    private var _message = MutableLiveData<String>()
    val message : LiveData<String>
        get() = _message

    private var _secretNumber = MutableLiveData<String>()
    val secretNumberValue : LiveData<String>
        get() = _secretNumber


    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain

    init{
        readTotalScore()
        _secretNumber.value = secretNumber
        _eventPlayAgain.value = false
        _score.value = currentTotal
        _totalGameScore.value = previousTotal + currentTotal
        _message.value = message
        Log.i("ResultViewModel", "Final score is $attempts")

    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    private fun readTotalScore() {
        viewModelScope.launch {
            userPreference.userTotalScore.collect {
                previousTotal = it
            }

        }
    }

    fun onResetTotalScore() {
        _score.value = 0
        _totalGameScore.value = 0
    }

    fun onPlayAgainComplete() {
        viewModelScope.launch {
            userPreference.incrementTotalScore( _totalGameScore.value?:0)
        }
        _eventPlayAgain.value = false
    }
}