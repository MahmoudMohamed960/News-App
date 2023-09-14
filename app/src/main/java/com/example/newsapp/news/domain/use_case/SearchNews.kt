package com.example.newsapp.news.domain.use_case

import androidx.paging.PagingData
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.domain.repo.NewsRepo
import kotlinx.coroutines.flow.Flow

class SearchNews(private val repo: NewsRepo) {
    operator fun invoke(query: String): Flow<PagingData<Articles>> {
        return repo.searchNews(query)
    }
}