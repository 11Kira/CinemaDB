package com.kira.android.cinemadb.features.reviews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.android.cinemadb.features.tv.TVRemoteSource
import com.kira.android.cinemadb.model.UserReviewResult

class TVShowReviewPagingSource(
    private val header: String,
    private val tvSeriesId: Long,
    private val remoteSource: TVRemoteSource
) : PagingSource<Int, UserReviewResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserReviewResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getTVShowReviews(header, tvSeriesId, page = currentPage)
            val tvShowReviews = response.body()?.results.orEmpty()

            LoadResult.Page(
                data = tvShowReviews,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (currentPage < response.body()?.totalPages!!) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserReviewResult>): Int? {
        return null
    }
}