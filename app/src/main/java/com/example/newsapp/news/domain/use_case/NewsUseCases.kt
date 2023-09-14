package com.example.newsapp.news.domain.use_case

data class NewsUseCases(
    val getNews: GetNews,
    val getFilterList: GetFilterList,
    val filterNews: FilterNews,
    val getFilterSelectedItems: GetFilterSelectedItems,
)
