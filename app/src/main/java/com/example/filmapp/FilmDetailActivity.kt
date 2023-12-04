package com.example.filmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.filmapp.api.FilmDetailService
import com.example.filmapp.api.MovieDetail
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FilmDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        val intent: Intent = intent
        val filmId: Int = intent.getIntExtra("com.example.filmapp.FILM_ID_EXTRAS", -1)

        val movieTitleTextView: TextView = findViewById(R.id.film_detail_title_text_view)
        val movieOverviewTextView: TextView = findViewById(R.id.film_detail_overview_content_text_view)
        val moviePosterImageView: ImageView = findViewById(R.id.film_detail_poster_image_view)
        val movieTagline: TextView = findViewById(R.id.film_detail_tagline_text_view)

        fun updateUI(data: MovieDetail?){
            if (data != null) {
                movieTitleTextView.text = data.title
                movieOverviewTextView.text = data.description
                movieTagline.text = data.tagline
                moviePosterImageView.load("https://image.tmdb.org/t/p/original${data.posterPath}")
            }
        }

        runBlocking {
            val movieDetailDeffered = async {
                getMovieDetailAPI(filmType = "movie", filmId = filmId)
            }
            val movieDetailData = movieDetailDeffered.await()
            if (movieDetailData != null){
                updateUI(movieDetailData)
            }

        }


    }
}

suspend fun getMovieDetailAPI(filmType: String, filmId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieDetail?{
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofitObject = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    return try {
        val service = retrofitObject.create(FilmDetailService::class.java)
        service.getFilmDetail(
            filmType = filmType,
            filmId = filmId,
            apiKey = apiKey
        )
    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getMovieDetailAPI: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        null
    }
}