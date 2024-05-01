package v.kira.cinemadb.features.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuItem(val label: String, val icon: ImageVector, val screenRoute: String) {
    object Home : BottomMenuItem("Home", Icons.Filled.Home, "home")
    object Favorites : BottomMenuItem("Favorites", Icons.Filled.Favorite, "favorites")
    object Watchlist : BottomMenuItem("Watch List", Icons.Filled.Check, "watchlist")
    object Account : BottomMenuItem("Account", Icons.Filled.Person, "account")
}
