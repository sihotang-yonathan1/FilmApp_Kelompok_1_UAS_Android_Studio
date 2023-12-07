package com.example.filmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.MovieDetailInfoService
import com.example.filmapp.api.model.MovieReviewModel
import com.example.filmapp.api.model.MovieReviewResult
import com.example.filmapp.layout_configuration.reviewMovie.ReviewMovieAdapter
import com.example.filmapp.layout_configuration.reviewMovie.ReviewMovieRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshiReview: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofitReviewObject: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
class FilmReviewActivity : AppCompatActivity(), ReviewMovieRecycleViewClickListener {
    private var movieReviewResultList = ArrayList<MovieReviewResult>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_review)


        val filmId: Int = intent.getIntExtra("com.example.filmapp.FILM_ID_EXTRAS", -1)
        Log.d("filmId", "onCreate: filmId: $filmId")

        val reviewMovieRecyclerView: RecyclerView = findViewById(R.id.film_review_recycler_view)
        val reviewMovieRecyclerViewViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reviewMovieRecyclerView.layoutManager = reviewMovieRecyclerViewViewLayoutManager

        fun setReviewAdapter(){
            val reviewMovieAdapter = ReviewMovieAdapter(movieReviewResultList, this)
            reviewMovieRecyclerView.adapter = reviewMovieAdapter
        }

        runBlocking {
            val movieReviewDeferred = async {
                getMovieReview(filmId)
            }

            val movieReviewPage = movieReviewDeferred.await()
            Log.d("movieReviewPage", "onCreate: movieReviewPage: $movieReviewPage")
            if (movieReviewPage != null){
                if (movieReviewPage.results.isNotEmpty()){
                    movieReviewResultList.clear()
                    for (reviewResult in movieReviewPage.results){
                        movieReviewResultList.add(reviewResult)
                    }
                    Log.d("movieReviewResultList", "onCreate: $movieReviewResultList")
                    setReviewAdapter()
                }

            }

        }

    }

    override fun onReviewItemClicked(position: Int) {
        Log.d("onReviewItemClicked", "onReviewItemClicked: pos: $position")
    }
}

suspend fun getMovieReview(movieId: Int, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieReviewModel?{
    return try {
        val service = retrofitReviewObject.create(MovieDetailInfoService::class.java);
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
