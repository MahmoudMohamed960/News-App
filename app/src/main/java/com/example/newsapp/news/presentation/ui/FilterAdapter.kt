package com.example.newsapp.news.presentation.ui

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FilterItemLayoutBinding
import com.example.newsapp.news.domain.model.FilterModel

class FilterAdapter(
    private val context: Context,
    val onClickItem: (Int, Int) -> Unit
) : ListAdapter<FilterModel, FilterAdapter.ViewHolder>(FilterListDiffCallback()) {
    private lateinit var filterItemLayoutBinding: FilterItemLayoutBinding
    var currentCountryindex = -1
    var previousCountryIndex = -1
    var currentCategoryIndex = -1
    var previousCategoryIndex = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        filterItemLayoutBinding =
            FilterItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(filterItemLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position), position)
    }

    class FilterListDiffCallback : DiffUtil.ItemCallback<FilterModel>() {
        override fun areItemsTheSame(oldItem: FilterModel, newItem: FilterModel) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: FilterModel,
            newItem: FilterModel
        ) =
            oldItem.name == newItem.name

        override fun getChangePayload(oldItem: FilterModel, newItem: FilterModel): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }


    inner class ViewHolder(private val binding: FilterItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(filterItem: FilterModel, position: Int) {
            binding.type.text = filterItem.name
            binding.root.setOnClickListener {
                if (filterItem.value != "") {
                    binding.tickIcon.visibility = View.VISIBLE
                    if (filterItem.type == "country") {
                        if (!filterItem.isSelected) {
                            previousCountryIndex = currentCountryindex
                            currentCountryindex = position
                            onClickItem(previousCountryIndex, currentCountryindex)
                        }
                    } else {
                        if (!filterItem.isSelected) {
                            previousCategoryIndex = currentCategoryIndex
                            currentCategoryIndex = position
                            onClickItem(previousCategoryIndex, currentCategoryIndex)
                        }
                    }
                }
            }
            if (filterItem.isSelected) {
                if (filterItem.type == "country")
                    currentCountryindex = position
                else
                    currentCategoryIndex = position
                binding.tickIcon.visibility = View.VISIBLE
            } else
                binding.tickIcon.visibility = View.GONE

            if (filterItem.value == "") {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.gray
                    )
                )
                binding.type.typeface = Typeface.DEFAULT_BOLD
                binding.tickIcon.visibility = View.GONE
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                binding.type.typeface = Typeface.DEFAULT
            }

        }
    }


}