package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.MovieResult

sealed class MovieState {
    data class ShowError(val error: Any): MovieState()
    data class SetPopularMovies(val popularMovies: List<MovieResult>): MovieState()
    data class SetTopRatedMovies(val topRatedMovies: List<MovieResult>): MovieState()
    data class SetNowPlayingMovies(val nowPlayingMovies: List<MovieResult>): MovieState()
    data class SetUpcomingMovies(val upcomingMovies: List<MovieResult>): MovieState()

}