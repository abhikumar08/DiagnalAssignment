package com.abhishek.diagnal.ui.movie_listing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.diagnal.data.MovieRepository
import com.abhishek.diagnal.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListingViewModel @ViewModelInject public constructor(private val repository: MovieRepository) :
    ViewModel() {
    val currentPage: MutableLiveData<Int> = MutableLiveData(0)
    val hasReachedMax: MutableLiveData<Boolean> = MutableLiveData(false)
    val movies: MutableLiveData<List<Movie>> = MutableLiveData(emptyList());
    val inSearchMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val filteredMovies: MutableLiveData<MutableList<Movie>> = MutableLiveData(mutableListOf())

    fun getMovies() {
        inSearchMode.value?.let {
            if (it) {
                return
            }
        }
        hasReachedMax.value?.let {
            if (it) {
                return
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val page = currentPage.value ?: 0
            val list = repository.getMovies(page+1)
            withContext(Dispatchers.Main){
                hasReachedMax.value = list.size < 20
                movies.value = movies.value!! + list
                currentPage.value = page + 1
            }
        }
    }


    fun searchMovies(query: String) {
        if (query.length<3){
            return
        }
        inSearchMode.postValue(true)
        filteredMovies.value?.clear()
        movies.value?.let {
            val matchingMovies = it.filter { movie -> movie.name?.contains(query, true) ?: false }
            filteredMovies.postValue(matchingMovies.toMutableList())
        }
        viewModelScope.launch {
            val matchingMovies = repository.searchMovies(query)
            filteredMovies.postValue(matchingMovies.toMutableList())
        }
    }

    fun resetSearch() {
        filteredMovies.postValue(mutableListOf())
        inSearchMode.postValue(false)
    }
}