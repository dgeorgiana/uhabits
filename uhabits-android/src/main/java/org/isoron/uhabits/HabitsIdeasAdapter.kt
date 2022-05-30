package org.isoron.uhabits

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HabitsIdeasAdapter(private val ideasList: List<MyHabitsIdea>): RecyclerView.Adapter<HabitsIdeasAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view  = LayoutInflater.from(parent.context).inflate(R.layout.habit_idea,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return ideasList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Response", "List Count :${ideasList.size} ")

        return holder.bind(ideasList[position])

    }
    class ViewHolder(itemView : View) :RecyclerView.ViewHolder(itemView) {


        var imageView = itemView.findViewById<ImageView>(R.id.ivFlag)
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var tvCases = itemView.findViewById<TextView>(R.id.tvCases)
        fun bind(country: MyHabitsIdea) {

            val name ="Cases :${country.cases.toString()}"
            tvTitle.text = country.country
            tvCases.text = name
            Picasso.get().load(country.countryInfo.flag).into(imageView)
        }

    }
}