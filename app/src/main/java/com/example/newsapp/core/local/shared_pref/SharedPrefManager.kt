package com.example.newsapp.core.local.shared_pref

import android.content.Context
import com.example.newsapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("NEWS_PREFS", Context.MODE_PRIVATE)

    fun saveSelectedCountry(value: String) {
        sharedPreferences.edit().putString(Constants.SELECTED_COUNTRY, value).apply()
    }

    fun getSelectedCountry() = sharedPreferences.getString(Constants.SELECTED_COUNTRY, "us") ?: "us"

    fun saveSelectedCategory(value: String?) {
        sharedPreferences.edit().putString(Constants.SELECTED_CATEGORY, value).apply()
    }

    fun getSelectedCategory() = sharedPreferences.getString(Constants.SELECTED_CATEGORY, null)

}