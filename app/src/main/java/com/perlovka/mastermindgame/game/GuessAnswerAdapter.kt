package com.perlovka.mastermindgame.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            return oldItem.number == newItem.number
        }
    }

    override fun submitList(list: List<Guess>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}