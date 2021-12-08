package com.perlovka.mastermindgame.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perlovka.mastermindgame.R
import com.perlovka.mastermindgame.model.Guess

class GuessAnswerAdapter:  RecyclerView.Adapter<GuessAnswerAdapter.ViewHolder>(){
    var data =  listOf<Guess>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    // Retrieve the item from the data list and bind text to viewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val guessNumber: TextView = itemView.findViewById(R.id.guess)
        val message: TextView = itemView.findViewById(R.id.message)
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_guess, parent, false)
                return ViewHolder(view)
            }
        }
        fun bind(item: Guess) {
            val res = itemView.context.resources
            guessNumber.text = item.number
            message.text = item.message
        }
    }

}