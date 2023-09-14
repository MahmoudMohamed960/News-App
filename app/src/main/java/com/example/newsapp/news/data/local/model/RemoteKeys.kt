package com.example.newsapp.news.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
