package com.example.filmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.MovieResult
import com.example.filmapp.api.MovieResultPage
import com.example.filmapp.api.PopularFilmService
import com.example.filmapp.layout_configuration.popularMovie.popularMovieAdapter
import com.example.filmapp.layout_configuration.popularMovie.popularMovieRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity(), popularMovieRecycleViewClickListener {
    val FILM_ID_EXTRAS = "com.example.filmapp.FILM_ID_EXTRAS"
    var data = ArrayList<MovieResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val popularRecyclerView: RecyclerView = findViewById(R.id.most_popular_movie_recycler_view)
        val popularRecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularRecyclerView.layoutManager = popularRecyclerViewLayoutManager

        fun setAdapter(){
            val moviePopularAdapter = popularMovieAdapter(data, this)
            popularRecyclerView.adapter = moviePopularAdapter
        }

        fun onPopularMovieClicked(view: View, popularMovie: popularMovieAdapter){
            Log.d("onPopularMovieClicked", "onPopularMovieClicked: Hello World!")
        }

        runBlocking {
//            var filmDetailDeffered = async {
//                getMovieDetailAPI(filmType = "movie", filmId = 1075794)
//            }

            var popularFilmDeffered = async {
                getPopularFilm(filmType = "movie")
            }

            // val filmDetail = filmDetailDeffered.await()
            val popularFilmPage = popularFilmDeffered.await()

            if (popularFilmPage != null) {
                data.addAll(popularFilmPage.results)
                setAdapter()
            }

            // Log.d("filmDetail", "onCreate: $filmDetail")
            Log.d("popularFilmPage", "onCreate: $popularFilmPage")
        }
    }

    override fun onPopularItemClicked(position: Int) {
        Log.d("OnPopularItemClicked", "onPopularItemClicked: pos: $position")
        val filmId: Int = data[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }
}



suspend fun getPopularFilm(filmType: String, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieResultPage? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit_obj = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    try {
        val service = retrofit_obj.create(PopularFilmService::class.java)
        return service.getPopularFilm(
            filmType = filmType,
            apiKey = apiKey
        )
    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getPopularFilm: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        return null
    }
}