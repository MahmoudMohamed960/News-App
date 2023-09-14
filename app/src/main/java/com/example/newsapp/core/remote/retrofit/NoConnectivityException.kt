package com.example.newsapp.core.remote.retrofit

import java.io.IOException

object NoConnectivityException : IOException() {
    fun errorMsg(): String {
        return "No Internet Connection"
    }
}