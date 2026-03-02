package com.kira.android.cinemadb.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kira.android.cinemadb.DetailsScreenNavigation
import com.kira.android.cinemadb.UserReviewScreenNavigation
import com.kira.android.cinemadb.features.account.watchlist.WatchlistScreen
import com.kira.android.cinemadb.features.details.DetailsScreen
import com.kira.android.cinemadb.features.movies.MovieListScreen
import com.kira.android.cinemadb.features.reviews.UserReviewListScreen
import com.kira.android.cinemadb.features.tv.TVShowListScreen

@Composable
fun NavigationGraph(navController: NavHostController, contentPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomMenuItem.Movies.screenRoute
    ) {
        composable(BottomMenuItem.Movies.screenRoute) {
            MovieListScreen(
                contentPadding,
                onItemClick = { movieId, type ->
                    navController.navigate(DetailsScreenNavigation(movieId, type))
                }
            )
        }
        composable(BottomMenuItem.TV.screenRoute) {
            TVShowListScreen(
                contentPadding,
                onItemClick = { tvShowId, type ->
                    navController.navigate(DetailsScreenNavigation(tvShowId, type))
                }
            )
        }
        composable(BottomMenuItem.Watchlist.screenRoute) {
            WatchlistScreen(
                contentPadding,
                onItemClick = { mediaId, type ->
                    navController.navigate(DetailsScreenNavigation(mediaId, type))
                }
            )
        }
        composable<DetailsScreenNavigation> {
            val args = it.toRoute<DetailsScreenNavigation>()
            DetailsScreen(
                args.id,
                args.type,
                onReviewClick = { mediaId, type ->
                    navController.navigate(UserReviewScreenNavigation(mediaId, type))
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