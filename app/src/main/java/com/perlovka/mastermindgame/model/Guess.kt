package com.perlovka.mastermindgame.model

data class Guess(var number: String = "", var message: String = "", var size : Int = 0, var guessRightNumberRightPositionCounter : Int = 0, var rightNumberWrongPositionCounter: Int = 0)