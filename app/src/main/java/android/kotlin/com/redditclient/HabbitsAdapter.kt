package android.kotlin.com.redditclient

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.single_card.view.*

class HabbitsAdapter(val habbits: List<Habit>) : RecyclerView.Adapter<HabbitsAdapter.HabbitViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabbitViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return HabbitViewHolder(view)
    }

    override fun getItemCount(): Int = habbits.size

    override fun onBindViewHolder(holder: HabbitViewHolder, position: Int) {
        val habit = habbits[position]
        holder.card.description.text = habit.description
        holder.card.tv_title.text = habit.title
        holder.card.image.setImageBitmap(habit.image)
    }

    class HabbitViewHolder(val card: View) : RecyclerView.ViewHolder(card)
}