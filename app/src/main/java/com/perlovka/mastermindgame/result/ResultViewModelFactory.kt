package com.perlovka.mastermindgame.result

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultViewModelFactory (private val finalScore: Int,
                              private val message: String, private val secretNumber: String, private val application: Application
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(finalScore, message, secretNumber, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}