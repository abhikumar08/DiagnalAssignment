package com.abhishek.diagnal.models
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesPage(
    @Json(name = "page")
    var page: Page?
)