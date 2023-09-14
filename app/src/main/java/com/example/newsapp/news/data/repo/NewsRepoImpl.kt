package com.example.newsapp.news.data.repo

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.R
import com.example.newsapp.core.local.room.ArticlesDataBase
import com.example.newsapp.core.local.shared_pref.SharedPrefManager
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.NewsService
import com.example.newsapp.news.domain.model.FilterModel
import com.example.newsapp.news.domain.model.FilterSelectedData
import com.example.newsapp.news.domain.repo.NewsRepo
import com.example.newsapp.util.Constants.ARTICLES_PER_PAGE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsService: NewsService,
    private val sharedPrefManager: SharedPrefManager,
    private val articlesDataBase: ArticlesDataBase,
    @ApplicationContext private val context: Context
) : NewsRepo {


    override fun getNews(): Flow<PagingData<Articles>> {

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = ARTICLES_PER_PAGE,
                initialLoadSize = ARTICLES_PER_PAGE,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(articlesDataBase, sharedPrefManager, newsService)
        ) {
            articlesDataBase.articleDao.getArticles()
        }.flow
    }

    override fun getFilterList() = listOf(
        FilterModel(
            "category",
            context.resources.getString(R.string.category_title),
            ""
        ),
        FilterModel(
            "category",
            context.resources.getString(R.string.all_news),
            ""
        ),
        FilterModel(
            "category",
            context.resources.getString(R.string.business),
            "business"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.entertainment),
            "entertainment"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.general),
            "general"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.health),
            "health"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.sports),
            "sports"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.science),
            "science"
        ), FilterModel(
            "category",
            context.resources.getString(R.string.technology),
            "technology"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.country_title),
            ""
        ),
        FilterModel(
            "country",
            context.resources.getString(R.string.usa),
            "us"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.argentina),
            "ar"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.austria),
            "au"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.china),
            "ch"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.colombia),
            "co"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.cuba),
            "cu"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.czech),
            "cz"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.germany),
            "de"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.egypt),
            "eg"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.france),
            "fr"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.great_britain),
            "gb"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.greece),
            "gr"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.hong_kong),
            "hk"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.hungary),
            "hu"
        ), FilterModel(
            "country",
            context.resources.getString(R.string.indonesia),
            "id"
        )
    )


    override  fun filterNews(filterSelectedData: FilterSelectedData) {
        sharedPrefManager.saveSelectedCountry(filterSelectedData.country)
        sharedPrefManager.saveSelectedCategory(filterSelectedData.category)
    }

    override fun getFilterSelectedItems() = FilterSelectedData(
        country = sharedPrefManager.getSelectedCountry(),
        category = sharedPrefManager.getSelectedCategory()
    )


}