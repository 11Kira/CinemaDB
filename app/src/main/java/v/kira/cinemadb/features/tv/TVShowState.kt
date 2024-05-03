package v.kira.cinemadb.features.tv

import v.kira.cinemadb.model.TVResult

sealed class TVShowState {
    data class ShowError(val error: Any): TVShowState()
    data class SetTrendingTVShows(val trendingTVShows: List<TVResult>): TVShowState()
    data class SetTopRatedTVShows(val topRatedTVShows: List<TVResult>): TVShowState()
    data class SetAiringTodayTVShows(val airingTodayTVShows: List<TVResult>): TVShowState() }