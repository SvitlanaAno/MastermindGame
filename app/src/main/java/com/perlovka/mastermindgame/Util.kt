package com.perlovka.mastermindgame

import kotlin.random.Random

/**
 * Returns a string representing the result of game.
 */
fun resultMessage(match: Int): String{
    val result = when(match) {
        4 -> " You win the game!"
        else -> "You lost the game..."
    }
    return result
}

/**
 * Returns a string representing the result of guesses.
 */
fun convertResultToMessage(match: Int, appearance: Int): String {
    if(match > 0) return "Guess a correct number and location"
    else if(appearance > 0) return "Guess a correct number"
    else return "Guess was incorrect"
}

/**
 * Returns hardcoded random secret number
 */
fun generateSecret(): String {


    val n = '0'..'7'
    val chars = n.toMutableList()
  //  val random = Random()
    return buildString {
        for (i in 1..4) {
            val number = chars[Random.nextInt(chars.size)]
            append(number)
        }
    }
}