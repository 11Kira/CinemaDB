package com.kira.android.cinemadb.features.reviews

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.android.cinemadb.features.movies.MovieRemoteSource
import com.kira.android.cinemadb.model.UserReviewResult

class MovieReviewPagingSource(
    private val header: String,
    private val movieId: Long,
    private val remoteSource: MovieRemoteSource
) : PagingSource<Int, UserReviewResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserReviewResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getMovieReviews(header, movieId, page = currentPage)
            val movieReviews = response.body()?.results.orEmpty()

            LoadResult.Page(
                data = movieReviews,
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