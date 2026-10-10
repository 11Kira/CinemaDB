package com.kira.android.cinemadb.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MoviesRoute

@Serializable
data object TVShowsRoute

@Serializable
data object WatchlistRoute

@Serializable
data class DetailsScreenRoute(val id: Long, val type: Int)

@Serializable
data class UserReviewScreenRoute(val id: Long, val type: Int)