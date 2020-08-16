package com.abhishek.diagnal.data

import com.abhishek.diagnal.models.Movie

interface MovieRepository {

    suspend fun getMovies(page: Int): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>
}