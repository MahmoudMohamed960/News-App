package com.example.newsapp.news.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.news.data.remote.model.Source
import java.text.SimpleDateFormat
import java.util.TimeZone

@Entity
data class Articles(
    @PrimaryKey()
    val id: String,
    val author: String?,
    val source: String?,
    val title: String?,
    val content: String?,
    val imageUrl: String?,
    var publishedAt: String?
) {
    var newPublishedAt = publishedAt
        get() = publishedAt?.let { getTimeAgoString(it) }
}

fun getTimeAgoString(time: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    try {
        // Parse the date string into a Date object
        val date = dateFormat.parse(time)

        // Convert the Date object to milliseconds since the Unix epoch
        val millisecondsSinceEpoch = date.time

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

    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}


