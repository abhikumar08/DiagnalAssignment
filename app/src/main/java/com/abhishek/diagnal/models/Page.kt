package com.abhishek.diagnal.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Page(
    @Json(name = "content-items")
    var movieItems: MovieItems?,
    @Json(name = "page-num")
    var pageNum: String?,
    @Json(name = "page-size")
    var pageSize: String?,
    @Json(name = "title")
    var title: String?,
    @Json(name = "total-content-items")
    var totalContentItems: String?
)