package v.kira.cinemadb.features.navigation

import v.kira.cinemadb.R

sealed class BottomMenuItem(val label: String, val icon: Int, val screenRoute: String) {
    data object Home : BottomMenuItem("Home", R.drawable.ic_movie, "home")
    data object TV : BottomMenuItem("TV Series", R.drawable.ic_tv, "tv")
    data object Watchlist : BottomMenuItem("Watch List", R.drawable.ic_list, "watchlist")
    data object Account : BottomMenuItem("Account", R.drawable.ic_account, "account")
}
