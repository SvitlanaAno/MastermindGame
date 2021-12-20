package com.perlovka.mastermindgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.databinding.ListItemGuessBinding
import com.perlovka.mastermindgame.model.Guess

class GuessAnswerAdapter: ListAdapter<Guess, GuessAnswerAdapter.ViewHolder>(GuessDiffCallback()){

    // Retrieve the item from the data list and bind text to viewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemGuessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Guess) {
            binding.guesses = item
            binding.executePendingBindings()
        }


       /* fun bind(item: Guess) {
            binding.guess.text = item.number
            binding.message.text = item.message
            binding.colorItem1.setBackgroundColor(
                when (item.guessRightNumberRightPositionCounter[0]) {
                    0 -> R.color.black
                    1 -> R.color.green_dark
                    2 -> R.color.green
                    else -> R.color.white
                }
            )
            binding.colorItem2.setBackgroundColor(
                when (item.guessRightNumberRightPositionCounter[1]) {
                    0 -> R.color.black
                    1 -> R.color.green_dark
                    2 -> R.color.green
                    else -> R.color.white
                }
            )

            binding.colorItem3.setBackgroundColor(
                when (item.guessRightNumberRightPositionCounter[2]) {
                    0 -> R.color.black
                    1 -> R.color.green_dark
                    2 -> R.color.green
                    else -> R.color.white
                }
            )
            binding.colorItem4.setBackgroundColor(
                when (item.guessRightNumberRightPositionCounter[3]) {
                    0 -> R.color.black
                    1 -> R.color.green_dark
                    2 -> R.color.green
                    else -> R.color.white
                }
            )
        }*/

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGuessBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
    class GuessDiffCallback :
        DiffUtil.ItemCallback<Guess>() {
        override fun areItemsTheSame(oldItem: Guess, newItem: Guess): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: Guess, newItem: Guess): Boolean {
           // return oldItem == newItem
            return oldItem == newItem
        }
    }

    override fun submitList(list: List<Guess>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}