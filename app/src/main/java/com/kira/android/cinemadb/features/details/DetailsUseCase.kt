package com.kira.android.cinemadb.features.details

import com.kira.android.cinemadb.features.movies.MovieRepository
import com.kira.android.cinemadb.features.tv.TVRepository
import com.kira.android.cinemadb.model.AccountStates
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository
) {

    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
    ): MovieResult {
        return movieRepository.getMovieDetails(token, movieId)
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
    ): TVShowResult {
        return tvRepository.getTVShowDetails(token, tvSeriesId)
    }

    suspend fun getMovieWatchlistDetails(
        token: String,
        movieId: Long,
    ): AccountStates {
        return movieRepository.getMovieWatchlistDetails(token, movieId)
    }

    suspend fun getTVShowWatchlistDetails(
        token: String,
        tvSeriesId: Long,
    ): AccountStates {
        return tvRepository.getTVShowWatchlistDetails(token, tvSeriesId)
    }
}