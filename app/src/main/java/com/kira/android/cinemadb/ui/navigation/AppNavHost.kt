package com.kira.android.cinemadb.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kira.android.cinemadb.features.account.watchlist.WatchlistScreen
import com.kira.android.cinemadb.features.details.DetailsScreen
import com.kira.android.cinemadb.features.list.movies.MovieListScreen
import com.kira.android.cinemadb.features.list.tv.TVShowListScreen
import com.kira.android.cinemadb.features.reviews.UserReviewListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    onScroll: (Float, Float) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MoviesRoute
    ) {
        composable<MoviesRoute> {
            MovieListScreen(
                contentPadding = contentPadding,
                onItemClick = { movieId, type ->
                    navController.navigate(DetailsScreenRoute(movieId, type))
                },
                onScroll = onScroll
            )
        }
        composable<TVShowsRoute> {
            TVShowListScreen(
                contentPadding = contentPadding,
                onItemClick = { tvShowId, type ->
                    navController.navigate(DetailsScreenRoute(tvShowId, type))
                },
                onScroll = onScroll
            )
        }
        composable<WatchlistRoute> {
            WatchlistScreen(
                contentPadding = contentPadding,
                onItemClick = { mediaId, type ->
                    navController.navigate(DetailsScreenRoute(mediaId, type))
                },
                onScroll = onScroll
            )
        }
        composable<DetailsScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailsScreenRoute>()
            DetailsScreen(
                mediaId = args.id,
                mediaType = args.type,
                onReviewClick = { mediaId, mediaType ->
                    navController.navigate(UserReviewScreenRoute(mediaId, mediaType))
                },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable<UserReviewScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<DetailsScreenRoute>()
            UserReviewListScreen(
                id = args.id,
                type = args.type,
                navController
            )
        }
    }
}