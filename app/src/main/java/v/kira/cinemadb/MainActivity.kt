package v.kira.cinemadb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import v.kira.cinemadb.Graph.DETAILS_SCREEN_ROUTE
import v.kira.cinemadb.features.account.watchlist.WatchlistScreen
import v.kira.cinemadb.features.details.DetailsScreen
import v.kira.cinemadb.features.movies.MovieListScreen
import v.kira.cinemadb.features.search.SearchScreen
import v.kira.cinemadb.features.tv.TVShowListScreen
import v.kira.cinemadb.navigation.BottomMenuItem

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreenView()
        }
    }

    companion object {
        const val TRENDING = 1
        const val NOW_PLAYING = 2
        const val TOP_RATED = 3
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            if (currentRoute(navController) != DETAILS_SCREEN_ROUTE) {
                BottomNavigation(navController = navController)
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableStateOf("Movies") }
    val screens = listOf(
        BottomMenuItem.Movies,
        BottomMenuItem.TV,
        BottomMenuItem.Search,
        BottomMenuItem.Watchlist
    )
    BottomNavigation(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.DarkGray,
    ) {
        screens.forEach {
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
                        color = if (selectedItem == it.label) Color.White else Color.Gray,
                        fontSize = 10.sp
                    ) },
                icon = {
                    Icon(
                        imageVector  = ImageVector.vectorResource(it.icon),
                        contentDescription = it.label,
                        tint = if (selectedItem == it.label) Color.White else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.Movies.screenRoute
    ) {
        composable(BottomMenuItem.Movies.screenRoute) {
            MovieListScreen(
                onItemClick = { movieId, type ->
                    navController.navigate("${Graph.DETAILS_GRAPH}/${movieId}/${type}")
                }
            )
        }
        composable(BottomMenuItem.TV.screenRoute) {
            TVShowListScreen(
                onItemClick = { tvShowId, type ->
                    navController.navigate("${Graph.DETAILS_GRAPH}/${tvShowId}/${type}")
                }
            )
        }
        composable(BottomMenuItem.Search.screenRoute) { SearchScreen(
            onItemClick = { mediaId, type ->
                navController.navigate("${Graph.DETAILS_GRAPH}/${mediaId}/${type}")
            }
        ) }
        composable(BottomMenuItem.Watchlist.screenRoute) { WatchlistScreen(
            onItemClick = { mediaId, type ->
                navController.navigate("${Graph.DETAILS_GRAPH}/${mediaId}/${type}")
            }
        ) }
        detailsNavGraph()
    }
}

fun NavGraphBuilder.detailsNavGraph() {

    navigation(
        route = "${Graph.DETAILS_GRAPH}/{id}/{type}",
        startDestination = DETAILS_SCREEN_ROUTE,
    ) {
        composable(
            route = DETAILS_SCREEN_ROUTE,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("type") {
                    type = NavType.StringType
                },
            )
        ) {
            val id = it.arguments?.getString("id")?.toLong() ?: 0L
            val type = it.arguments?.getString("type")?.toInt() ?: 0
            DetailsScreen(id, type)
        }
    }
}

object Graph {
    const val DETAILS_GRAPH = "details_graph"
    const val DETAILS_SCREEN_ROUTE = "details/{id}/{type}"
}