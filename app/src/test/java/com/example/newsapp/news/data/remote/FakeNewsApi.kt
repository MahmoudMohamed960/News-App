package com.example.newsapp.news.data.remote

import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.model.Article
import com.example.newsapp.news.data.remote.model.NewsResponse
import com.example.newsapp.news.data.remote.model.Source
import java.io.IOException

class FakeNewsApi : NewsService {
    var errorMsg: String? = null
    var fakeArticles = ArrayList<Article>()
    override suspend fun getNews(
        page: Int,
        pageSize: Int,
        country: String?,
        category: String?,
        query: String?
    ): NewsResponse {
        errorMsg?.let {
            throw IOException(it)
        }
        (0..50).forEach { item ->
            fakeArticles.add(
                Article(
                    author = item.toString(),
                    content = item.toString(),
                    title = item.toString(),
                    source = Source(item.toString(), item.toString()),
                    urlToImage = item.toString(),
                    publishedAt = item.toString(),
                    description = item.toString(),
                    url = item.toString()
                )
            )
        }
        return NewsResponse(
            articles = fakeArticles,
            status = "ok",
            code = "200",
            message = null,
            totalResults = 50
        )
    }
}