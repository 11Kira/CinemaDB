package com.kira.android.cinemadb.features.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kira.android.cinemadb.domain.mapMovieDetailsToDomain
import com.kira.android.cinemadb.features.reviews.MovieReviewPagingSource
import com.kira.android.cinemadb.model.AccountStates
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.UserReviewResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteSource: MovieRemoteSource
) {
    fun getTopRatedMovies(token: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TopRatedMoviePagingSource(token, remoteSource)
        }.flow

    fun getTrendingMovies(token: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TrendingMoviePagingSource(token, remoteSource)
        }.flow

    fun getMovieReviews(token: String, movieId: Long): Flow<PagingData<UserReviewResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            MovieReviewPagingSource(token, movieId, remoteSource)
        }.flow

    suspend fun getMovieDetails(token: String, movieId: Long): MovieResult {
        return remoteSource.getMovieDetails(token, movieId).mapMovieDetailsToDomain()
    }

    suspend fun getMovieWatchlistDetails(token: String, movieId: Long): AccountStates {
        return remoteSource.getMovieWatchlistDetails(token, movieId)
    }
}