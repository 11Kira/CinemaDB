package com.kira.android.cinemadb.features.tv

import androidx.paging.PagingData
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.model.UserReviewResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TVUseCase @Inject constructor(
    private val repository: TVRepository
) {
    fun getTopRatedTVShows(
        token: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTopRatedTVShows(token)
    }

    fun getTrendingTVShows(
        token: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTrendingTVShows(token)
    }

    fun getTVShowReviews(token: String, tvSeriesId: Long): Flow<PagingData<UserReviewResult>> {
        return repository.getTVShowReviews(token, tvSeriesId)
    }
}