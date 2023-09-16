package com.example.newsapp.news.data.repo

import android.content.Context
import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.newsapp.core.local.room.ArticlesDataBase
import com.example.newsapp.core.local.shared_pref.SharedPrefManager
import com.example.newsapp.news.data.local.model.Articles
import com.example.newsapp.news.data.remote.FakeNewsApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@ExperimentalPagingApi
@RunWith(RobolectricTestRunner::class)
class NewsRemoteMediatorTest {
    lateinit var mockDB: ArticlesDataBase
    private lateinit var sharedPreferences: SharedPreferences
    var mockApi = FakeNewsApi()
    private var fakeArticles = ArrayList<Articles>()

    @Mock
    lateinit var mockContext: Context


    @Before
    fun setup() {
        mockContext = Mockito.mock(Context::class.java)
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        `when`(
            mockContext.getSharedPreferences(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(sharedPreferences)

        mockDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArticlesDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()


    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val remoteMediator = NewsRemoteMediator(
            mockDB,
            SharedPrefManager(sharedPreferences = sharedPreferences),
            mockApi
        )
        val pagingState = PagingState<Int, Articles>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )


        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

    }


    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        val remoteMediator = NewsRemoteMediator(
            mockDB,
            SharedPrefManager(sharedPreferences = sharedPreferences),
            mockApi
        )
        val pagingState = PagingState<Int, Articles>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        mockApi.errorMsg = "test error"
        val remoteMediator = NewsRemoteMediator(
            mockDB,
            SharedPrefManager(sharedPreferences = sharedPreferences),
            mockApi
        )
        val pagingState = PagingState<Int, Articles>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }



    @After
    @Throws(IOException::class)
    fun closeDb() {
        mockDB.close()
    }


}