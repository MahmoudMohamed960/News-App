package com.example.newsapp.news.domain.use_case

import androidx.paging.PagingData
import com.example.newsapp.core.remote.retrofit.Response
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.domain.repo.NewsRepo
import kotlinx.coroutines.flow.Flow

class GetNews(private val repo: NewsRepo) {
     operator fun invoke(): Flow<PagingData<Articles>> {
        return repo.getNews()
    }
}