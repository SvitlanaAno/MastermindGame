package com.perlovka.mastermindgame.model

data class Guess(var number: String = "_ _ _ _", var message: String = "", var letters : Int = 0)