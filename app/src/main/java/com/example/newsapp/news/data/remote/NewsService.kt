package com.example.newsapp.news.data.remote

import com.example.newsapp.news.data.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("country") country: String,
        @Query("category") category: String
    ): NewsResponse
}