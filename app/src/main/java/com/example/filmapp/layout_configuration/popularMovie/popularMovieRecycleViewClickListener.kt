package com.example.filmapp.layout_configuration.popularMovie

import android.view.View
import com.example.filmapp.api.MovieResult

interface popularMovieRecycleViewClickListener {
    fun onPopularItemClicked(position: Int)
}