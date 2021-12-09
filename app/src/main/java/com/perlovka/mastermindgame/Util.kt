package com.perlovka.mastermindgame

/**
 * Returns a string representing the result of game.
 */
fun resultMessage(match: Int): String{
    if(match == 4)
        return " You win the game!"
    else
        return "You lost the game..."
}

/**
 * Returns a string representing the result of guesses.
 */
fun convertResultToMessage(match: Int, appearance: Int): String {
    if(match > 0) return "Guess a correct number and location"
    else if(appearance > 0) return "Guess a correct number"
    else return "Guess was incorrect"
}