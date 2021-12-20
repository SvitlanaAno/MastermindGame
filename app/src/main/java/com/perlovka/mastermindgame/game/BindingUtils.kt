package com.perlovka.mastermindgame.game
import android.widget.TextView
import androidx.databinding.BindingAdapter
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