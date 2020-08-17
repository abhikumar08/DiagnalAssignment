package com.abhishek.diagnal.data

import android.content.Context
import com.abhishek.diagnal.R
import com.abhishek.diagnal.models.Movie
import com.abhishek.diagnal.models.MoviesPage
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl(private val moshi: Moshi, private val context: Context) :
    MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val jsonFile = when (page) {
                1 -> R.raw.api_response_page_1
                2 -> R.raw.api_response_page_2
                3 -> R.raw.api_response_page_3
                else -> R.raw.api_response_page_1
            }
            val jsonString = context.resources.openRawResource(jsonFile)
                .bufferedReader().use { it.readText() }

            val adapter: JsonAdapter<MoviesPage> = moshi.adapter(MoviesPage::class.java)
            val moviesPage = adapter.fromJson(jsonString)
            moviesPage?.page?.movieItems?.movie
        } ?: emptyList()
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        val allMovies = emptyList<Movie>().toMutableList()
        return withContext(Dispatchers.IO) {
            for (page in 1..3)
                allMovies.addAll(getMovies(page))
            allMovies.filter { it.name?.contains(query, ignoreCase = true) ?: false }
        }
    }


}