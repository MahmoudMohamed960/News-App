package com.example.newsapp.news.data.remote.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val code: String?,
    val message: String?,
    val totalResults: Int
)