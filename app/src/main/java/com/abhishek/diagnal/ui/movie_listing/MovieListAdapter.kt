package com.abhishek.diagnal.ui.movie_listing

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.diagnal.R
import com.abhishek.diagnal.base.BaseRecyclerViewAdapter
import com.abhishek.diagnal.databinding.ItemMovieLayoutBinding
import com.abhishek.diagnal.models.Movie
import com.bumptech.glide.Glide

class MovieListAdapter(movies: List<Movie>) :
    BaseRecyclerViewAdapter<Movie, MovieListAdapter.MovieItemViewHolder>(movies.toMutableList()) {


    class MovieItemViewHolder(val binding: ItemMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(movie: Movie) {
            binding.textView.text = movie.name
            Glide.with(binding.imageView.context)
                .load(Uri.parse("file:///android_asset/${movie.posterImage}"))
                .placeholder(R.drawable.placeholder_for_missing_posters)
                .into(binding.imageView);
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val binding: ItemMovieLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie_layout,
            parent,
            false
        );
        return MovieItemViewHolder(binding);
    }


    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    override fun setData(list: List<Movie>) {
        val diffCallback = MovieDiffCallback(this.mList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mList.clear()
        mList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}