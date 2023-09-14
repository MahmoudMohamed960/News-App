package com.example.newsapp.news.presentation.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.LoadStateLayoutBinding

class NewsLoadStateAdapter() :
    LoadStateAdapter<NewsLoadStateAdapter.NewsLoadStateViewHolder>() {

    inner class NewsLoadStateViewHolder(
        private val loadStateLayoutBinding: LoadStateLayoutBinding
    ) : RecyclerView.ViewHolder(loadStateLayoutBinding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                loadStateLayoutBinding.textViewError.text = loadState.error.localizedMessage
            }
            when (loadState) {
                is LoadState.Loading -> {
                    loadStateLayoutBinding.progressbar.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    loadStateLayoutBinding.buttonRetry.visibility = View.VISIBLE
                    loadStateLayoutBinding.textViewError.visibility = View.VISIBLE

                }

                else -> {
                    loadStateLayoutBinding.progressbar.visibility = View.VISIBLE

                }
            }

        }

    }

    override fun onBindViewHolder(holder: NewsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NewsLoadStateViewHolder {
        return NewsLoadStateViewHolder(
            LoadStateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}