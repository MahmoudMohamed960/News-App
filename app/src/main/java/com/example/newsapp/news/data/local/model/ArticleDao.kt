package com.example.newsapp.news.data.local.model

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getArticles(): PagingSource<Int, Articles>

    @Insert
    suspend fun insertArticles(article: List<Articles>)

    @Query("DELETE  FROM articles")
    suspend fun clearArticles()
}