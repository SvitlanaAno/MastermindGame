package com.perlovka.mastermindgame.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel(attempts : Int, message: String) : ViewModel() {
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _message = MutableLiveData<String>()
    val message : LiveData<String>
        get() = _message

    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain

    init{
        _eventPlayAgain.value = false
        _score.value = attempts * 5
        _message.value = message
        Log.i("ResultViewModel", "Final score is $attempts")
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }
}