package com.example.newsapp.news.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsapp.core.local.room.ArticlesDataBase
import com.example.newsapp.core.local.shared_pref.SharedPrefManager
import com.example.newsapp.core.remote.retrofit.NoConnectivityException
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.local.model.RemoteKeys
import com.example.newsapp.news.data.remote.NewsService
import com.example.newsapp.util.Constants.ARTICLES_PER_PAGE
import com.example.newsapp.util.Constants.NEWS_STARTING_PAGE_INDEX
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.ArrayList
import javax.inject.Inject
import kotlin.math.ceil

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val articlesDataBase: ArticlesDataBase,
    private val sharedPrefManager: SharedPrefManager,
    private val newsService: NewsService
) : RemoteMediator<Int, Articles>() {
    private var numberOfPages = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Articles>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    NEWS_STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }


            }

            val response = newsService.getNews(
                page,
                ARTICLES_PER_PAGE,
                sharedPrefManager.getSelectedCountry(),
                sharedPrefManager.getSelectedCategory(),
                null
            )
            if (response.status == "ok") {
                numberOfPages = ceil((response.totalResults / ARTICLES_PER_PAGE).toDouble()).toInt()
                if (numberOfPages == 0)
                    numberOfPages += 1
                val articles = response.articles
                val endOfPaginationReached = articles.isEmpty()
                val prevKey = if (page == NEWS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = ArrayList<RemoteKeys>()
                val articlesList = ArrayList<Articles>()
                articles.map { remoteArticle ->
                    val randomNumber = (0..100000).random()
                    val id = remoteArticle.source.name + randomNumber
                    remoteKeys.add(
                        RemoteKeys(
                            articleId = id,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    )
                    articlesList.add(
                        Articles(
                            id = id,
                            author = remoteArticle.author,
                            title = remoteArticle.title,
                            imageUrl = remoteArticle.urlToImage,
                            content = remoteArticle.content,
                            source = remoteArticle.source.name,
                            publishedAt = remoteArticle.publishedAt
                        )
                    )
                }


                articlesDataBase.withTransaction {
                    // save data locally
                    if (loadType == LoadType.REFRESH) {
                        articlesDataBase.remoteKeysDao.clearRemoteKeys()
                        articlesDataBase.articleDao.clearArticles()
                    }
                    articlesDataBase.articleDao.insertArticles(articlesList)
                    articlesDataBase.remoteKeysDao.insertRemoteKeys(remoteKeys)
                }


                MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached
                )
            } else {
                MediatorResult.Error(
                    Exception(
                        "SERVER_ERROR"
                    )
                )
            }


        } catch (exception: Exception) {
            return when (exception) {
                is SocketTimeoutException -> {
                    MediatorResult.Error(exception)
                }

                is NoConnectivityException -> {
                    MediatorResult.Error(Exception(NoConnectivityException.errorMsg()))
                }

                else -> {
                    MediatorResult.Error(
                        Exception(
                            "SERVER_ERROR"
                        )
                    )
                }
            }
        }
    }


    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, Articles>): RemoteKeys? {
        return state.pages.lastOrNull()
        {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()
            ?.let { article ->
                articlesDataBase.remoteKeysDao.remoteKeysArticleId(article.id)
            }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, Articles>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }
            ?.data?.firstOrNull()
            ?.let { article ->
                articlesDataBase.remoteKeysDao.remoteKeysArticleId(article.id)
            }
    }

}
