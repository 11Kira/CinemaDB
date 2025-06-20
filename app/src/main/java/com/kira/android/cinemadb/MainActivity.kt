package com.kira.android.cinemadb

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
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
import com.kira.android.cinemadb.Graph.DETAILS_SCREEN_ROUTE
import com.kira.android.cinemadb.features.account.watchlist.WatchlistScreen
import com.kira.android.cinemadb.features.details.DetailsScreen
import com.kira.android.cinemadb.features.movies.MovieListScreen
import com.kira.android.cinemadb.features.search.SearchScreen
import com.kira.android.cinemadb.features.tv.TVShowListScreen
import com.kira.android.cinemadb.navigation.BottomMenuItem
import dagger.hilt.android.AndroidEntryPoint

private lateinit var viewModel: MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val injectedViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        viewModel = injectedViewModel
        setContent {
            MainScreenView()
        }
    }

    companion object {
        const val TRENDING = 1
        const val TOP_RATED = 2
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        bottomBar = {
            if (currentRoute(navController) != DETAILS_SCREEN_ROUTE) {
                BottomNavigation(navController = navController)
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)

    ) { contentPadding ->
        Box(modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .consumeWindowInsets(contentPadding)
            .systemBarsPadding()
        ) {
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
    var selectedItem by remember { mutableStateOf("movies") }
    val selectedTab by remember { mutableStateOf(viewModel.currentlySelectedTab) }

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
                selected = (selectedItem == selectedTab.collectAsState().value),
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
                    viewModel.updateSelectedTab(it.label)
                },
                label = {
                    Text(
                        text = it.label,
                        color = if (selectedTab.collectAsState().value == it.label) Color.White else Color.Gray,
                        fontSize = 10.sp,
                        fontFamily = if (selectedItem == selectedTab.collectAsState().value) Font(R.font.roboto_bold).toFontFamily() else Font(R.font.roboto_medium).toFontFamily(),
                    ) },
                icon = {
                    Icon(
                        imageVector  = ImageVector.vectorResource(it.icon),
                        contentDescription = it.label,
                        tint = if (selectedTab.collectAsState().value == it.label) Color.White else Color.Gray
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
        composable(BottomMenuItem.Search.screenRoute) {
            SearchScreen(
                onItemClick = { mediaId, type ->
                    navController.navigate("${Graph.DETAILS_GRAPH}/${mediaId}/${type}")
                }
            )
        }
        composable(BottomMenuItem.Watchlist.screenRoute) {
            WatchlistScreen(
                onItemClick = { mediaId, type ->
                    navController.navigate("${Graph.DETAILS_GRAPH}/${mediaId}/${type}")
                }
            )
        }
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