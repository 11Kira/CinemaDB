package com.kira.android.cinemadb.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kira.android.cinemadb.DetailsScreenNavigation
import com.kira.android.cinemadb.UserReviewScreenNavigation
import com.kira.android.cinemadb.features.account.watchlist.WatchlistScreen
import com.kira.android.cinemadb.features.details.DetailsScreen
import com.kira.android.cinemadb.features.list.movies.MovieListScreen
import com.kira.android.cinemadb.features.list.tv.TVShowListScreen
import com.kira.android.cinemadb.features.reviews.UserReviewListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    contentPadding: PaddingValues,
    onScroll: (Float, Float) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.Movies.screenRoute
    ) {
        composable(BottomMenuItem.Movies.screenRoute) {
            MovieListScreen(
                contentPadding = contentPadding,
                onItemClick = { movieId, type ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate(DetailsScreenNavigation(movieId, type))
                    }
                },
                onScroll = onScroll
            )
        }
        composable(BottomMenuItem.TV.screenRoute) {
            TVShowListScreen(
                contentPadding = contentPadding,
                onItemClick = { tvShowId, type ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate(DetailsScreenNavigation(tvShowId, type))
                    }
                },
                onScroll = onScroll
            )
        }
        composable(BottomMenuItem.Watchlist.screenRoute) {
            WatchlistScreen(
                contentPadding = contentPadding,
                onItemClick = { mediaId, type ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate(DetailsScreenNavigation(mediaId, type))
                    }
                },
                onScroll = onScroll
            )
        }
        composable<DetailsScreenNavigation> {
            val args = it.toRoute<DetailsScreenNavigation>()
            DetailsScreen(
                args.id,
                args.type,
                onReviewClick = { mediaId, type ->
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.navigate(UserReviewScreenNavigation(mediaId, type))
                    }
                },
                navController
            )
        }

        composable<UserReviewScreenNavigation> {
            val args = it.toRoute<UserReviewScreenNavigation>()
            UserReviewListScreen(
                args.id,
                args.type,
                navController
            )
        }
    }
}