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
import com.example.filmapp.api.MovieDetailInfoService
import com.example.filmapp.api.model.MovieRecommendationModel
import com.example.filmapp.api.model.MovieReviewModel
import com.example.filmapp.api.model.MovieSimilarResultModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofitObject: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

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
            // deferred item
            val movieDetailDeffered = async {
                getMovieDetailAPI(filmType = "movie", filmId = filmId)
            }
            val movieReviewDeferred = async { getMovieReview(movieId = filmId) }
            val movieSimilarDeferred = async { getMovieSimilar(movieId = filmId) }
            val movieKeywordDeferred = async { getMovieRecommendation(movieId = filmId) }

            // await the deferred
            val movieDetailData = movieDetailDeffered.await()
            val movieReview = movieReviewDeferred.await()
            val movieSimilar = movieSimilarDeferred.await()
            val movieKeyword = movieKeywordDeferred.await()

            Log.d("movieReview", "onCreate: $movieReview")
            Log.d("movieSimilar", "onCreate: $movieSimilar")
            Log.d("movieKeyword", "onCreate: $movieKeyword")

            if (movieDetailData != null){
                updateUI(movieDetailData)
            }

        }


    }
}

suspend fun getMovieDetailAPI(filmType: String, filmId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieDetail?{
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

suspend fun getMovieRecommendation(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieRecommendationModel?{
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        return service.getRecommendation(
            movieId = movieId,
            apiKey = apiKey
        )

    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getMovieRecommendation: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        null
    }
}

suspend fun getMovieSimilar(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieSimilarResultModel?{
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        return service.getSimilarMovie(
            movieId = movieId,
            apiKey = apiKey
        )

    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getMovieSimilar: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        null
    }
}

suspend fun getMovieReview(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieReviewModel?{
    return try {
        val service = retrofitObject.create(MovieDetailInfoService::class.java);
        return service.getMovieReview(
            movieId = movieId,
            apiKey = apiKey
        )

    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getMovieReview: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        null
    }
}

