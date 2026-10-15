package com.kira.android.cinemadb.data.remote.source.tv

import com.kira.android.cinemadb.data.remote.service.TVService
import com.kira.android.cinemadb.model.AccountStates
import com.kira.android.cinemadb.model.TVShowResult
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

    suspend fun getTVShowReviews(
        token: String,
        tvSeriesId: Long,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getTVShowReviews(
            header = token,
            seriesId = tvSeriesId,
            page = page
        )
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
    ): TVShowResult = withContext(Dispatchers.IO) {
        tvService.getTVShowDetails(
            header = token,
            seriesId = tvSeriesId,
        )
    }

    suspend fun getTVShowWatchlistDetails(
        token: String,
        tvSeriesId: Long,
    ): AccountStates = withContext(Dispatchers.IO) {
        tvService.getTVShowWatchlistDetails(
            header = token,
            seriesId = tvSeriesId,
        )
    }
}