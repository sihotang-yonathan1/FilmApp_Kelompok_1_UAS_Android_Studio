package com.example.filmapp.api.model

import com.squareup.moshi.Json

data class MovieReviewAuthorDetails(
    val name: String,
    val username: String,
    @Json(name = "avatar_path") val avatarPath: String? = null,
    @Json(name = "rating") val reviewNumber: Double? = null,
)

data class MovieReviewResult(
    @Json(name = "author") val authorName: String,
    @Json(name = "author_details") val authorDetails: MovieReviewAuthorDetails,
    val content: String

)

data class MovieReviewModel(
    @Json(name = "id") val movieId: Int,
    @Json(name = "page") val pageNum: Int,
    val results: List<MovieReviewResult>
)
