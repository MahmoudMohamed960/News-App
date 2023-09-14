package com.example.newsapp.news.domain.use_case

import com.example.newsapp.news.domain.model.FilterSelectedData
import com.example.newsapp.news.domain.repo.NewsRepo

class FilterNews(private val repo: NewsRepo) {
     operator fun invoke(country: String, category: String) {
        repo.filterNews(
            filterSelectedData = FilterSelectedData(country, category)
        )
    }
}