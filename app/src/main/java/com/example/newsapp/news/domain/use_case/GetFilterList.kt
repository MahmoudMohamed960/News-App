package com.example.newsapp.news.domain.use_case

import com.example.newsapp.news.domain.repo.NewsRepo

class GetFilterList(private val repo: NewsRepo) {
    operator fun invoke() = repo.getFilterList()
}