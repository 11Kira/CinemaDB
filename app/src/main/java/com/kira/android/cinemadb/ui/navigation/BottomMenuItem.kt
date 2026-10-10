package com.kira.android.cinemadb.ui.navigation

import com.kira.android.cinemadb.R

sealed class BottomMenuItem(val label: String, val icon: Int, val route: Any) {
    data object Movies : BottomMenuItem("Movies", R.drawable.ic_movie, MoviesRoute)
    data object TV : BottomMenuItem("TV Series", R.drawable.ic_tv, TVShowsRoute)
    data object Watchlist : BottomMenuItem("Watchlist", R.drawable.ic_list, WatchlistRoute)
}