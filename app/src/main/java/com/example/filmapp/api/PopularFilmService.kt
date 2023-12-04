package com.example.filmapp.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class MovieResult (
    val id: Int,
    val title: String,
    @Json(name = "overview") val description: String,
    @Json(name = "poster_path") val posterPath: String,
)

data class MovieResultPage (
    @Json(name = "page") val pageNum: Int,
    val results: List<MovieResult>
)

interface PopularFilmService {
    @GET("discover/{type}")
    suspend fun getPopularFilm(
        @Path("type") filmType: String,
        @Query("api_key") apiKey: String,
    ): MovieResultPage
}