package v.kira.cinemadb.features.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuItem(val label: String, val icon: ImageVector, val screenRoute: String) {
    data object Home : BottomMenuItem("Home", Icons.Filled.Home, "home")
    data object Favorites : BottomMenuItem("Favorites", Icons.Filled.Favorite, "favorites")
    data object Watchlist : BottomMenuItem("Watch List", Icons.Filled.CheckCircle, "watchlist")
    data object Account : BottomMenuItem("Account", Icons.Filled.Person, "account")
}
