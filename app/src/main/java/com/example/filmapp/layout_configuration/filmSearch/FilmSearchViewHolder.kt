package com.example.filmapp.layout_configuration.filmSearch

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filmapp.R
import com.example.filmapp.api.MovieResult

class FilmSearchViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: FilmSearchRecycleViewClickListener):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.search_item_template, parent, false)), OnClickListener{


    private var moviePosterImage: ImageView? = null
    private var movieTitle: TextView? = null


    init {
        moviePosterImage = itemView.findViewById(R.id.film_search_item_image_view)
        movieTitle = itemView.findViewById(R.id.film_search_item_film_name)

        itemView.setOnClickListener(this)
    }

    fun bind (data: MovieResult){
        movieTitle?.text = data.title
        if (data.posterPath != null) {
            moviePosterImage?.load("https://image.tmdb.org/t/p/w300${data.posterPath}")
        }

    }

    override fun onClick(v: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION){
            listener.onSearchItemClicked(position)
        }
    }
}