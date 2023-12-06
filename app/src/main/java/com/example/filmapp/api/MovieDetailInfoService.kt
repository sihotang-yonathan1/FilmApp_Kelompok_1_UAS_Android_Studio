package com.example.filmapp.api

import com.example.filmapp.api.model.MovieRecommendationModel
import com.example.filmapp.api.model.MovieReviewModel
import com.example.filmapp.api.model.MovieSimilarResultModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieDetailInfoService {
    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendation(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieRecommendationModel

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieSimilarResultModel

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReview(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): MovieReviewModel

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImage(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    )

    @GET("movie/{movie_id}/external_ids")
    suspend fun getExternalId(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    )

    @GET("movie/{movie_id}/keywords")
    suspend fun getMovieKeyword(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    )
}