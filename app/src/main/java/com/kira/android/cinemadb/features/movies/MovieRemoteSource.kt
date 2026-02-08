package com.kira.android.cinemadb.features.movies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteSource @Inject constructor(
    private val movieService: MovieService
) {
    suspend fun getPopularMovies(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getPopularMovies(
            header = token,
            page = page,
        )
    }

    suspend fun getTopRatedMovies(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getTopRatedMovies(
            header = token,
            page = page
        )
    }

    suspend fun getMovieDetails(
        token: String,
        movieId: Long
    ) = withContext(Dispatchers.IO) {
        movieService.getMovieDetails(
            header = token,
            movieId = movieId
        )
    }

    suspend fun getMovieWatchlistDetails(
        token: String,
        movieId: Long
    ) = withContext(Dispatchers.IO) {
        movieService.getMovieWatchlistDetails(
            header = token,
            movieId = movieId
        )
    }
}