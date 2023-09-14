package com.example.newsapp.news.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.domain.use_case.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
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

    private val _headerTitle = MutableLiveData<String?>()
    val headerTitle: LiveData<String?> = _headerTitle


    private val _filterDataChanged = MutableLiveData<Boolean?>()
    val filterDataChanged: LiveData<Boolean?> = _filterDataChanged

    fun changeHeaderTitle(title: String) {
        _headerTitle.value = title
    }

    fun changeFilterData(country: String, category: String?) {
        newsUseCases.filterNews(
            country = country,
            category = category
        )
        _filterDataChanged.value = true
        _headerTitle.value = category
    }

    fun getFilterSelectedItems() = newsUseCases.getFilterSelectedItems()

    fun searchNews(query: String) =
        newsUseCases.searchNews(query).cachedIn(viewModelScope).flowOn(Dispatchers.IO)

}