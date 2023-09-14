package com.example.newsapp.news.data.local.model

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.TimeZone

@Entity
data class Articles(
    @PrimaryKey
    val id: String,
    val author: String?,
    val source: String?,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    var publishedAt: String?
) {
    val newPublishedAt
        get() = publishedAt?.let { getTimeAgoString(it) }
}

@SuppressLint("SimpleDateFormat")
fun getTimeAgoString(time: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date = dateFormat.parse(time)
        val millisecondsSinceEpoch = date?.time
        if (millisecondsSinceEpoch != null) {

            val currentTimeMillis = System.currentTimeMillis()
            val timeDifferenceMillis = currentTimeMillis - millisecondsSinceEpoch

            val seconds = timeDifferenceMillis / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return when {
                days > 0 -> "$days ${if (days == 1L) "day" else "days"} ago"
                hours > 0 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
                minutes > 0 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
                else -> "$seconds ${if (seconds == 1L) "second" else "seconds"} ago"
            }
        } else
            return ""

    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}


