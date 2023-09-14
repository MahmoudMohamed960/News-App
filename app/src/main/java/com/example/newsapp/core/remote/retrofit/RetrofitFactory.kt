package com.example.newsapp.core.remote.retrofit

import android.content.Context
import com.example.newsapp.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitFactory @Inject constructor( context: Context) {
    private val httpBuilder =
        OkHttpClient
            .Builder()
            .addInterceptor(InternetStateInterceptor(context))
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url =
                    originalHttpUrl.newBuilder().addQueryParameter("apiKey", Constants.API_KEY)
                        .build()
                request.url(url)
                return@addInterceptor chain.proceed(request.build())
            }
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    private val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .client(httpBuilder)
        .build()

    fun <T> createService(service: Class<T>): T {
        return api.create(service)
    }

}