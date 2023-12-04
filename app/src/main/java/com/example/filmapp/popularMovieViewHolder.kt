package com.example.filmapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.MovieResult

class popularMovieViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_film_item, parent, false)) {

    private var movieTitle: TextView? = null
    private var movieDescription: TextView? = null

    init {
        movieTitle = itemView.findViewById(R.id.movie_item_label)
        movieDescription = itemView.findViewById(R.id.movie_item_description_text_view)
    }

    fun bind (data: MovieResult){
        movieTitle?.text = data.title
        movieDescription?.text = data.description

    }
}