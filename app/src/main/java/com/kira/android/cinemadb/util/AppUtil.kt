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

    fun formatReleaseYear(startDate: String?, endDate: String? = null): String {
        // Extract the first 4 characters (the year) safely
        val startYear = startDate?.take(4) ?: ""
        val endYear = endDate?.take(4) ?: ""

        return when {
            // Case: TV Series with both dates (2024-2026)
            startYear.isNotEmpty() && endYear.isNotEmpty() -> "$startYear-$endYear"

            // Case: Movie or TV Series with only start date (2026)
            startYear.isNotEmpty() -> startYear

            // Fallback
            else -> "N/A"
        }
    }
}