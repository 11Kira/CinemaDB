package com.kira.android.cinemadb.features.movies

import com.kira.android.cinemadb.model.MovieResult

sealed class MovieState {
    data class ShowError(val error: Any): MovieState()
    data class SetTrendingMovies(val trendingMovies: List<MovieResult>): MovieState()
    data class SetTopRatedMovies(val topRatedMovies: List<MovieResult>): MovieState()
    data class SetMovieDetails(val movieDetails: MovieResult): MovieState()
    data class SetMovie(val movie: MovieResult): MovieState()

}