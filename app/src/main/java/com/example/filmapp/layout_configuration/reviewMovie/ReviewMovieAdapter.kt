package com.example.filmapp.layout_configuration.reviewMovie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.model.MovieCastInfo
import com.example.filmapp.api.model.MovieReviewResult

class ReviewMovieAdapter(val data: ArrayList<MovieReviewResult>, val listener: ReviewMovieRecycleViewClickListener): RecyclerView.Adapter<ReviewMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReviewMovieViewHolder(inflater, parent, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReviewMovieViewHolder, position: Int) {
        holder.bind(data[position])
    }
}