package v.kira.cinemadb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import v.kira.cinemadb.features.movies.MovieListScreen
import v.kira.cinemadb.features.navigation.AccountScreen
import v.kira.cinemadb.features.navigation.BottomMenuItem
import v.kira.cinemadb.features.navigation.SearchScreen
import v.kira.cinemadb.features.navigation.TVScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }

    companion object {
        const val POPULAR = 1
        const val NOW_PLAYING = 2
        const val TOP_RATED = 3
        const val UPCOMING = 4
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableStateOf("Home") }
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.TV,
        BottomMenuItem.Search,
        BottomMenuItem.Account
    )
    Box(modifier = Modifier.fillMaxSize()) {
        BottomNavigation(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            backgroundColor = Color.DarkGray,
        ) {
            items.forEach {
                BottomNavigationItem(
                    selected = (selectedItem == it.label),
                    onClick = {
                        selectedItem = it.label
                        navController.navigate(it.screenRoute) {
                            navController.graph.startDestinationRoute?.let { screenRoute ->
                                popUpTo(screenRoute) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(
                            text = it.label,
                            color = Color.White,
                            fontSize = 10.sp
                        ) },
                    icon = {
                        Icon(
                            imageVector  = ImageVector.vectorResource(it.icon),
                            contentDescription = it.label,
                            tint = Color.White
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomMenuItem.Home.screenRoute) {
        composable(BottomMenuItem.Home.screenRoute) { MovieListScreen() }
        composable(BottomMenuItem.TV.screenRoute) { TVScreen() }
        composable(BottomMenuItem.Search.screenRoute) { SearchScreen() }
        composable(BottomMenuItem.Account.screenRoute) { AccountScreen() }
    }
}