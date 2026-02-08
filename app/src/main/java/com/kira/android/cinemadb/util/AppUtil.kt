package com.kira.android.cinemadb.util

object AppUtil {
    fun retrievePosterImageUrl(posterUrl: String): String {
        return "https://image.tmdb.org/t/p/original/$posterUrl"
    }

    fun formatRuntime(totalMinutes: Int): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }
}