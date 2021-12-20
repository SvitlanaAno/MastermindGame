package com.perlovka.mastermindgame.model

data class Guess(var number: String = "", var message: String = "", var size : Int = 0, var guessRightNumberRightPositionCounter : MutableList<Int> = mutableListOf(0,0,0,0), var rightNumberWrongPositionCounter: Int = 0)