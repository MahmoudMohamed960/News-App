package com.example.newsapp.news.domain.repo

import androidx.paging.PagingData
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.domain.model.FilterModel
import com.example.newsapp.news.domain.model.FilterSelectedData
import kotlinx.coroutines.flow.Flow

interface NewsRepo {
    fun getNews(): Flow<PagingData<Articles>>
    fun filterNews(filterSelectedData: FilterSelectedData)
    fun getFilterSelectedItems(): FilterSelectedData
    fun getFilterList(): List<FilterModel>
}