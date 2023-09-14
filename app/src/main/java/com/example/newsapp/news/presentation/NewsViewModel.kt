package com.example.newsapp.news.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.core.remote.retrofit.Response
import com.example.newsapp.core.remote.retrofit.Status
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.model.NewsResponse
import com.example.newsapp.news.domain.model.FilterModel
import com.example.newsapp.news.domain.use_case.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsUseCases: NewsUseCases) : ViewModel() {

    val articlesFlow = newsUseCases.getNews().cachedIn(viewModelScope).flowOn(Dispatchers.IO)

    private val _selectedArticle = MutableLiveData<Articles>()
    val selectedArticle: LiveData<Articles> = _selectedArticle

    fun changeSelectedArticle(article: Articles) {
        _selectedArticle.value = article
    }

    fun getFilterData() = newsUseCases.getFilterList()

    private val _filterDataChanged = MutableLiveData<String>()
    val filterDataChanged: LiveData<String> = _filterDataChanged

    fun changeFilterData(country: String, category: String) {
        newsUseCases.filterNews(
            country = country,
            category = category
        )
        _filterDataChanged.value = category.toString()
    }

    fun getFilterSelectedItems() = newsUseCases.getFilterSelectedItems()


}