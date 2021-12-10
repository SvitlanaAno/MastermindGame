package com.perlovka.mastermindgame

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