package com.example.filmapp.layout_configuration.popularMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.MovieResult

class popularMovieAdapter(val data: ArrayList<MovieResult>, val listener: popularMovieRecycleViewClickListener): RecyclerView.Adapter<popularMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return popularMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: popularMovieViewHolder, position: Int) {
        holder.bind(data[position])
    }
}