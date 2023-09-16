package com.example.newsapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.newsapp.core.local.room.ArticlesDataBase
import com.example.newsapp.core.local.shared_pref.SharedPrefManager
import com.example.newsapp.core.remote.retrofit.RetrofitFactory
import com.example.newsapp.news.data.remote.NewsService
import com.example.newsapp.news.data.repo.NewsRepoImpl
import com.example.newsapp.news.domain.repo.NewsRepo
import com.example.newsapp.news.domain.use_case.GetFilterList
import com.example.newsapp.news.domain.use_case.GetFilterSelectedItems
import com.example.newsapp.news.domain.use_case.GetNews
import com.example.newsapp.news.domain.use_case.NewsUseCases
import com.example.newsapp.news.domain.use_case.FilterNews
import com.example.newsapp.news.domain.use_case.SearchNews
import com.example.newsapp.util.Constants.NEWS_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitFactory(application: Application) = RetrofitFactory(application)

    @Provides
    @Singleton
    fun provideNewsService(retrofit: RetrofitFactory) =
        retrofit.createService(NewsService::class.java)

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): ArticlesDataBase {
        return Room.databaseBuilder(
            context,
            ArticlesDataBase::class.java,
            ArticlesDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideDispatcher() = Dispatchers

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context)=
        context.getSharedPreferences(NEWS_PREFERENCES, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSharedPref(sharedPreferences: SharedPreferences) =
        SharedPrefManager(sharedPreferences)


    @Provides
    @Singleton
    fun provideNewsRepository(
        newsService: NewsService,
        sharedPrefManager: SharedPrefManager,
        articlesDataBase: ArticlesDataBase,
        @ApplicationContext context: Context
    ): NewsRepo =
        NewsRepoImpl(newsService, sharedPrefManager, articlesDataBase, context)


    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NewsRepoImpl): NewsUseCases {
        return NewsUseCases(
            getNews = GetNews(repository),
            filterNews = FilterNews(repository),
            getFilterList = GetFilterList(repository),
            getFilterSelectedItems = GetFilterSelectedItems(repository),
            searchNews = SearchNews(repository)
        )
    }
}