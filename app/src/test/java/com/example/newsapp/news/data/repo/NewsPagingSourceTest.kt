package com.example.newsapp.news.data.repo


import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

import androidx.paging.testing.TestPager
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.FakeNewsApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert.*



@ExperimentalPagingApi
@RunWith(JUnit4::class)
class NewsPagingSourceTest {
    var mockApi = FakeNewsApi()
    private var fakeArticles = ArrayList<Articles>()
    private var query = ""


    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() = runBlocking {
        loadFakeData()
        val pagingSource = NewsPagingSource(
            mockApi,
            query
        )

        val pager = TestPager(PagingConfig(10), pagingSource)
        val result = pager.refresh() as PagingSource.LoadResult.Page
        assertEquals(result.data,fakeArticles)

    }

    @Test
    fun refresh_returnError() = runBlocking{
        mockApi.errorMsg = "error"
        val pagingSource = NewsPagingSource(
            mockApi,
            query
        )

        val pager = TestPager(PagingConfig(10), pagingSource)
        val result = pager.refresh()
        assertTrue(result is PagingSource.LoadResult.Error)

    }

    private fun loadFakeData() {
        (0..50).forEach { item ->
            fakeArticles.add(
                Articles(
                    id = "",
                    author = item.toString(),
                    content = item.toString(),
                    title = item.toString(),
                    source = item.toString(),
                    imageUrl = item.toString(),
                    publishedAt = item.toString()
                )
            )
        }
    }


}