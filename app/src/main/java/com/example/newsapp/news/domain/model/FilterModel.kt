package com.example.newsapp.news.domain.model

data class FilterModel(
    val type: String,
    val name: String,
    val value: String,
    var isSelected: Boolean=false
)
