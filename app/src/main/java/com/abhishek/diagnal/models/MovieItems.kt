package com.abhishek.diagnal.models
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieItems(
    @Json(name = "content")
    var movie: List<Movie>?
)