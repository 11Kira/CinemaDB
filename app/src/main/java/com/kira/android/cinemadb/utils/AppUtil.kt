package com.kira.android.cinemadb.utils

object AppUtil {
    fun retrievePosterImageUrl(posterUrl: String?): String {
        if (posterUrl.isNullOrEmpty()) return ""
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

    fun formatReleaseYear(startDate: String?, endDate: String? = null): String {
        val startYear = startDate?.take(4) ?: ""
        val endYear = endDate?.take(4) ?: ""

        return when {
            startYear.isNotEmpty() && endYear.isNotEmpty() -> "$startYear-$endYear"
            startYear.isNotEmpty() -> startYear
            else -> "N/A"
        }
    }
}
