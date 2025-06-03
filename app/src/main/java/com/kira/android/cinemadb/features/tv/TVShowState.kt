package com.kira.android.cinemadb.features.tv

import com.kira.android.cinemadb.model.TVShowResult

sealed class TVShowState {
    data class ShowError(val error: Any): TVShowState()
    data class SetTrendingTVShows(val trendingTVShows: List<TVShowResult>): TVShowState()
    data class SetTopRatedTVShows(val topRatedTVShows: List<TVShowResult>): TVShowState()
}