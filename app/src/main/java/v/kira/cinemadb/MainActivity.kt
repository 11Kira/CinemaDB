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
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import v.kira.cinemadb.Graph.DETAILS_SCREEN_ROUTE
import v.kira.cinemadb.features.details.DetailsScreen
import v.kira.cinemadb.features.movies.MovieListScreen
import v.kira.cinemadb.features.navigation.AccountScreen
import v.kira.cinemadb.features.navigation.BottomMenuItem
import v.kira.cinemadb.features.navigation.SearchScreen
import v.kira.cinemadb.features.tv.TVShowListScreen


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
        const val UPCOMING = 4
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableStateOf("Home") }
    val screens = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.TV,
        BottomMenuItem.Search,
        BottomMenuItem.Account
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

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.Home.screenRoute
    ) {
        composable(BottomMenuItem.Home.screenRoute) {
            MovieListScreen(
                onItemClick = { movieId ->
                    navController.navigate("${Graph.DETAILS_GRAPH}/${movieId}")
                }
            )
        }
        composable(BottomMenuItem.TV.screenRoute) { TVShowListScreen() }
        composable(BottomMenuItem.Search.screenRoute) { SearchScreen() }
        composable(BottomMenuItem.Account.screenRoute) { AccountScreen() }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {

    navigation(
        route = "${Graph.DETAILS_GRAPH}/{movieId}",
        startDestination = DETAILS_SCREEN_ROUTE,
    ) {
        composable(
            route = DETAILS_SCREEN_ROUTE,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.StringType
                },
            )
        ) {
            val movieId = it.arguments?.getString("movieId")?.toLong() ?: 0L
            DetailsScreen(movieId = movieId)
        }
    }
}

object Graph {
    const val DETAILS_GRAPH = "details_graph"
    const val DETAILS_SCREEN_ROUTE = "details/{movieId}"
}

