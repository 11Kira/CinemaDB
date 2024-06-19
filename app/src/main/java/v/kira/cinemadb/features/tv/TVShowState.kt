package v.kira.cinemadb.features.tv

import v.kira.cinemadb.model.TVShowResult

sealed class TVShowState {
    data class ShowError(val error: Any): TVShowState()
    data class SetTrendingTVShows(val trendingTVShows: List<TVShowResult>): TVShowState()
    data class SetTopRatedTVShows(val topRatedTVShows: List<TVShowResult>): TVShowState()
}