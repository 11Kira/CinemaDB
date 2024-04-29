package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.CinemaResult

sealed class MovieState {
    data class ShowError(val error: Any): MovieState()
    data class SetPopularMovies(val popularMovies: List<CinemaResult>): MovieState()
    data class SetTopRatedMovies(val popularMovies: List<CinemaResult>): MovieState()
    data class SetNowPlayingMovies(val popularMovies: List<CinemaResult>): MovieState()
}