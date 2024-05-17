package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.MovieResult

sealed class MovieState {
    data class ShowError(val error: Any): MovieState()
    data class SetTrendingMovies(val trendingMovies: List<MovieResult>): MovieState()
    data class SetTopRatedMovies(val topRatedMovies: List<MovieResult>): MovieState()
    data class SetNowPlayingMovies(val nowPlayingMovies: List<MovieResult>): MovieState()
}