package v.kira.cinemadb.navigation

import v.kira.cinemadb.R

sealed class BottomMenuItem(val label: String, val icon: Int, val screenRoute: String) {
    data object Movies : BottomMenuItem("Movies", R.drawable.ic_movie, "movies")
    data object TV : BottomMenuItem("TV Series", R.drawable.ic_tv, "tv")
    data object Search : BottomMenuItem("Search", R.drawable.ic_search, "search")
    data object Watchlist : BottomMenuItem("Watchlist", R.drawable.ic_list, "watchlist")
}
