package com.perlovka.mastermindgame.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResultViewModelFactory (private val finalScore: Int,
                              private val message: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(finalScore, message) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}