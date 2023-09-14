package com.example.newsapp.news.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.NewsCardItemBinding
import com.example.newsapp.news.data.local.model.Articles

class ArticlesAdapter(
    val context: Context,
    val onClickItem: (Articles) -> Unit
) :
    PagingDataAdapter<Articles, ArticlesAdapter.ViewHolder>(ArticlesListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NewsCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindData(item)
    }

    class ArticlesListDiffCallback : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Articles,
            newItem: Articles
        ) = oldItem == newItem

        override fun getChangePayload(oldItem: Articles, newItem: Articles): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    inner class ViewHolder(private val binding: NewsCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: Articles?) {
            binding.title.text = item?.title
            binding.publishedAt.text = item?.newPublishedAt
            binding.source.text = item?.source
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(item?.imageUrl)
                .placeholder(circularProgressDrawable)
                .into(binding.artcleImage)

            binding.root.setOnClickListener {
                if (item != null) {
                    onClickItem(item)
                }
            }
        }
    }
}