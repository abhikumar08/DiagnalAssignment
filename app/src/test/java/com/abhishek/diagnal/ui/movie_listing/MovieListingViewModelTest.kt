package com.abhishek.diagnal.ui.movie_listing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.Observer
import com.abhishek.diagnal.data.MovieRepository
import com.abhishek.diagnal.models.Movie
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext


@RunWith(MockitoJUnitRunner::class)
class MovieListingViewModelTest {

    lateinit var SUT: MovieListingViewModel

    @Mock
    lateinit var repository: MovieRepository

    @Mock
    lateinit var hasReachedMaxObserver: Observer<Boolean>

    @Rule
    @JvmField
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private var testDispatcher = TestCoroutineDispatcher()
    private var testCoroutineScope = TestCoroutineScope()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        SUT = MovieListingViewModel(repository)
        SUT.hasReachedMax.observeForever(hasReachedMaxObserver)
    }

    @Test
    fun getMovies_lessThan20Movies_hasReachedMaxShouldBeTrue()  {
        val list = List(5) {
            Movie("", "")
        }
        testCoroutineScope.launch(testDispatcher){
            whenever(repository.getMovies(ArgumentMatchers.anyInt())).thenReturn(list)
            SUT.getMovies()
            assertEquals(true, SUT.hasReachedMax.value)
        }

    }

    @Test
    fun getMovies_20MoviesFetched_hasReachedMaxShouldBeFalse() {
        val list = List(20) {
            Movie("", "")
        }

        testCoroutineScope.launch(testDispatcher){
            `when`(repository.getMovies(ArgumentMatchers.anyInt())).thenReturn(list)
            SUT.getMovies()
            assertEquals(SUT.hasReachedMax.value, false)
        }
    }

    @Test
    fun getMovies_20MoviesFetched_currentPageShouldbeIncreased() {
        val list = List(20) {
            Movie("", "")
        }
        val page =1
        testCoroutineScope.launch(testDispatcher){
            `when`(repository.getMovies(page)).thenReturn(list)
            SUT.getMovies()
            assertEquals(page + 1, SUT.currentPage.value)
        }
    }

    @Test fun searchMovies_ifQueryLengthGreaterThanEqual3_searchMoviesShouldBeCalled(){
        val query = "Fam"
        testCoroutineScope.launch(testDispatcher){
            SUT.searchMovies(query)
            verify(repository, times(1)).searchMovies(query)
        }
    }

    @Test fun searchMovies_ifQueryLengthLessThan3_searchMoviesShouldNotBeCalled(){
        val query = "Fa"
        testCoroutineScope.launch(testDispatcher){
            SUT.searchMovies(query)
            verify(repository, never()).searchMovies(query)
        }
    }



}

