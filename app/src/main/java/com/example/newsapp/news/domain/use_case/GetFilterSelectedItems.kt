package com.example.newsapp.news.domain.use_case

import com.example.newsapp.news.domain.repo.NewsRepo

class GetFilterSelectedItems(private val repo: NewsRepo) {
    operator fun invoke() = repo.getFilterSelectedItems()
}