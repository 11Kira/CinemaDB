package com.kira.android.cinemadb.util

object AppUtil {
    fun retrievePosterImageUrl(posterUrl: String): String {
        return "https://image.tmdb.org/t/p/original/$posterUrl"
    }
}