package com.example.newsapp.news.data.repo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.core.remote.retrofit.NoConnectivityException
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.NewsService
import com.example.newsapp.util.Constants.ARTICLES_PER_PAGE
import com.example.newsapp.util.Constants.NEWS_STARTING_PAGE_INDEX
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.ArrayList
import kotlin.math.ceil

class NewsPagingSource(private val newsService: NewsService, private val query: String) :
    PagingSource<Int, Articles>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        val currentPage = params.key ?: NEWS_STARTING_PAGE_INDEX
        try {
        val response = newsService.getNews(
            page = currentPage,
            pageSize = ARTICLES_PER_PAGE,
            query = query
        )

            val articlesList = ArrayList<Articles>()
            response.articles.map { article ->
                articlesList.add(
                    Articles(
                        id = "",
                        author = article.author,
                        title = article.title,
                        imageUrl = article.urlToImage,
                        content = article.content,
                        source = article.source.name,
                        publishedAt = article.publishedAt
                    )
                )
            }
            var numberOfPages = ceil((response.totalResults / ARTICLES_PER_PAGE).toDouble()).toInt()
            if (numberOfPages == 0)
                numberOfPages += 1
            return LoadResult.Page(
                data = articlesList,
                prevKey = when (currentPage) {
                    NEWS_STARTING_PAGE_INDEX -> null
                    else -> currentPage - 1
                },
                nextKey = if (currentPage == numberOfPages)
                    null
                else
                    currentPage + 1
            )

        } catch (exception: Exception) {
            return when (exception) {
                is SocketTimeoutException -> {
                    LoadResult.Error(exception)
                }

                is NoConnectivityException -> {
                    LoadResult.Error(Exception(NoConnectivityException.errorMsg()))
                }

                else -> {
                    LoadResult.Error(
                        Exception(
                            "SERVER_ERROR"
                        )
                    )
                }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}