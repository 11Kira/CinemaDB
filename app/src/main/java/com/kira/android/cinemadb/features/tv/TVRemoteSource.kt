package com.kira.android.cinemadb.features.tv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TVRemoteSource @Inject constructor(
    private val tvService: TVService
) {
    suspend fun getTrendingTVShows(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getTrendingTVShows(
            header = token,
            page = page,
        )
    }

    suspend fun getTopRatedTVShows(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getTopRatedTVShows(
            header = token,
            page = page
        )
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
    ) = withContext(Dispatchers.IO) {
        tvService.getTVShowDetails(
            header = token,
            seriesId = tvSeriesId,
        )
    }

    suspend fun getTVShowWatchlistDetails(
        token: String,
        tvSeriesId: Long,
    ) = withContext(Dispatchers.IO) {
        tvService.getTVShowWatchlistDetails(
            header = token,
            seriesId = tvSeriesId,
        )
    }
}