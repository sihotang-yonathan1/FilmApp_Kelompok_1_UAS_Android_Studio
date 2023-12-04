package com.example.filmapp

import android.view.View
import com.example.filmapp.api.MovieResult

interface popularMovieRecycleViewClickListener {
    fun onPopularItemClicked(view: View, popularMovie: MovieResult)
}