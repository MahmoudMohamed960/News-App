package com.example.newsapp.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.news.data.local.model.ArticleDao
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.local.model.RemoteKeys
import com.example.newsapp.news.data.local.model.RemoteKeysDao

@Database(
    entities = [Articles::class,RemoteKeys::class],
    version = 5
)
abstract class ArticlesDataBase : RoomDatabase() {
    abstract val articleDao: ArticleDao
    abstract val remoteKeysDao:RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "articles_db"
    }
}