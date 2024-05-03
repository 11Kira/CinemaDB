package v.kira.cinemadb.features.tv

import v.kira.cinemadb.model.CinemaResult

sealed class TVState {
    data class ShowError(val error: Any): TVState()
    data class SetPopularTVShows(val popularTVShows: List<CinemaResult>): TVState()
    data class SetTopRatedTVShows(val topRatedTVShows: List<CinemaResult>): TVState()
    data class SetNowAiringTVShows(val nowAiringTVShows: List<CinemaResult>): TVState() }