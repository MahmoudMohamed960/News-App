package com.example.newsapp.core.local.shared_pref

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SharedPrefManagerTest {
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setup()  {
        sharedPreferences = mock(SharedPreferences::class.java)
        mockContext = mock(Context::class.java)
        `when`(
            mockContext.getSharedPreferences(
                anyString(),
                anyInt()
            )
        ).thenReturn(sharedPreferences)
        sharedPrefManager = SharedPrefManager(sharedPreferences)

    }

    @Test
    fun get_selected_country_from_shared_pref() {
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn("Egypt")
        assertTrue(sharedPrefManager.getSelectedCountry() == "Egypt")
    }

    @Test
    fun get_selected_category_from_shared_pref()  {
        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)
        assertTrue(sharedPrefManager.getSelectedCategory() == null)
    }

}