package com.abhishek.diagnal.models
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "name")
    var name: String?,
    @Json(name = "poster-image")
    var posterImage: String?
) : Parcelable