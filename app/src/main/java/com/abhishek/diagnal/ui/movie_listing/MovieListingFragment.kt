package com.abhishek.diagnal.ui.movie_listing

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.abhishek.diagnal.R
import com.abhishek.diagnal.databinding.MovieListingFragmentBinding
import com.abhishek.diagnal.utils.DebouncingQueryTextListener
import com.abhishek.diagnal.utils.EndlessRecyclerViewScrollListener
import com.abhishek.diagnal.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListingFragment : Fragment() {

    lateinit var binding: MovieListingFragmentBinding
    private val moviesAdapter = MovieListAdapter(emptyList())

    companion object {
        @JvmStatic
        fun newInstance() =
            MovieListingFragment()
    }

    private val viewModel: MovieListingViewModel by viewModels<MovieListingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.movie_listing_fragment, container, false
            )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                this@MovieListingFragment.lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isNotEmpty()) {
                        viewModel.searchMovies(it)
                    }else{
                        viewModel.resetSearch()
                    }
                }
            }
        )
        searchView.setOnCloseListener {
            viewModel.resetSearch()
            return@setOnCloseListener true
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val orientation = activity?.resources?.configuration?.orientation
        val (span, gridLayoutManger) = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Pair(7, GridLayoutManager(context, 7))
        } else {
            Pair(3, GridLayoutManager(context, 3))

        }
        binding.moviesRecyclerView.apply {
            layoutManager = gridLayoutManger
            addItemDecoration(GridSpacingItemDecoration(span, 30, true))
            adapter = moviesAdapter
            addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager as GridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getMovies()
                }
            })
        }
        viewModel.movies.value?.let {
            if (it.isEmpty()) {
                viewModel.getMovies()
            }
        }

        viewModel.inSearchMode.observe(viewLifecycleOwner, Observer {
            if (!it) {
                moviesAdapter.setData(viewModel.movies.value!!)
            }
        })
        viewModel.filteredMovies.observe(viewLifecycleOwner, Observer {
            if (viewModel.inSearchMode.value!!)
                moviesAdapter.setData(it)
        })
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            moviesAdapter.setData(it)
        })
    }


}