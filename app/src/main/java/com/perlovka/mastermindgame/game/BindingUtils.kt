package com.perlovka.mastermindgame.game
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.model.Guess

@BindingAdapter("guessNumber")
fun TextView.setGuessNumber(item: Guess?) {
    item?.let {
        text = item.number
    }
}

@BindingAdapter("guessResultMessage")
fun TextView.setGuessResultMessage(item: Guess?) {
    item?.let {
        text = item.message
    }
}
@BindingAdapter("guessInRightPosition")
fun TextView.setGuessInRightPosition(item: Guess?){
    item?.let{
        text = item.guessRightNumberRightPositionCounter.toString()
    }
}

@BindingAdapter("guessInWrongPosition")
fun TextView.setGuessInWrongPosition(item: Guess?){
    item?.let{
        text = item.rightNumberWrongPositionCounter.toString()
    }
}

@BindingAdapter("guess1")
fun TextView.setGuessColorPosition1(item: Guess?) {
    item?.let {
        setBackgroundColor(
            when (item.guessRightNumberRightPositionCounter[0]) {
                0 -> R.color.black
                1 -> R.color.green_dark
                2 -> R.color.green
                else -> R.color.white
            }
        )
    }
}

@BindingAdapter("guess2")
fun TextView.setGuessColorPosition2(item: Guess?) {
    item?.let {
        setBackgroundColor(
            when (item.guessRightNumberRightPositionCounter[1]) {
                0 -> R.color.black
                1 -> R.color.green_dark
                2 -> R.color.green
                else -> R.color.white
            }
        )
    }
}
@BindingAdapter("guess3")
fun TextView.setGuessColorPosition3(item: Guess?) {
    item?.let {
        setBackgroundColor(
            when (item.guessRightNumberRightPositionCounter[2]) {
                0 -> R.color.black
                1 -> R.color.green_dark
                2 -> R.color.green
                else -> R.color.white
            }
        )
    }
}
@BindingAdapter("guess4")
fun TextView.setGuessColorPosition4(item: Guess?) {
    item?.let {
        setBackgroundColor(
            when (item.guessRightNumberRightPositionCounter[3]) {
                0 -> R.color.black
                1 -> R.color.green_dark
                2 -> R.color.green
                else -> R.color.white
            }
        )
    }
}