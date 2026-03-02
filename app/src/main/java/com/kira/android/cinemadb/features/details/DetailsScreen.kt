package com.kira.android.cinemadb.features.details

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.components.CircularIconButton
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.util.AppUtil
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.roundToInt

lateinit var viewModel: DetailsViewModel

@Composable
fun DetailsScreen(
    id: Long,
    type: Int,
    onReviewClick: (Long, Int) -> Unit,
    navController: NavController
) {
    viewModel = hiltViewModel()
    MainScreen(navController, type, viewModel.movieState, onReviewClick)
    when (type) {
        1 -> {
            viewModel.getMovieDetails(id)
        }

        2 -> {
            viewModel.getTVShowDetails(id)
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    type: Int,
    sharedFlow: SharedFlow<DetailsState>,
    onReviewClick: (Long, Int) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedMovie by remember { mutableStateOf<MovieResult?>(null) }
    var selectedTVShow by remember { mutableStateOf<TVShowResult?>(null) }

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is DetailsState.SetMovieDetails -> {
                        selectedMovie = state.movieDetails
                    }

                    is DetailsState.SetTvShowDetails -> {
                        selectedTVShow = state.tvShowDetails
                    }

                    is DetailsState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }

    selectedMovie?.let {
        SetupMovieDetails(
            movie = it,
            tvShow = null,
            type,
            navController,
            onReviewClick
        )
    }
    selectedTVShow?.let {
        SetupMovieDetails(
            movie = null,
            tvShow = it,
            type,
            navController,
            onReviewClick
        )
    }

    //selectedTVShow?.let { SetupTVShowDetails(tvShow = it, navController, onReviewClick) }
}

@Composable
fun SetupMovieDetails(
    movie: MovieResult? = null,
    tvShow: TVShowResult? = null,
    type: Int,
    navController: NavController,
    onReviewClick: (movieId: Long, type: Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        ) {
            val posterPath = AppUtil.retrievePosterImageUrl(
                if (type == 1) {
                    movie?.posterPath.toString()
                } else {
                    tvShow?.posterPath.toString()
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = posterPath,
                    contentDescription = "Poster",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_video),
                    error = painterResource(id = R.drawable.ic_video)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                        .align(Alignment.TopCenter)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontFamily = Font(R.font.roboto_bold).toFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    text =
                        if (type == 1) {
                            movie?.tagline?.ifBlank { movie.title }.toString()
                        } else {
                            tvShow?.tagline?.ifBlank { tvShow.name }.toString()
                        },
                    color = Color.White
                )

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 20.dp),
                    text = "Title:",
                    color = Color.White
                )

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth(),
                    text = if (type == 1) {
                        movie?.title.toString()
                    } else {
                        tvShow?.name.toString()
                    },
                    color = Color.White
                )

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 20.dp),
                    text = "Cast:",
                    color = Color.White
                )

                val casts = ArrayList<String>()
                if (type == 1) {
                    movie?.credits?.cast?.forEach { cast ->
                        if (casts.size < 7) casts.add(cast.name)
                    }
                } else {
                    tvShow?.credits?.cast?.forEach { cast ->
                        if (casts.size < 7) casts.add(cast.name)
                    }
                }

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    text = TextUtils.join(", ", casts),
                    color = Color.White
                )

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 20.dp),
                    text = "Details:",
                    color = Color.White
                )

                val genres = ArrayList<String>()
                if (type == 1) {
                    movie?.genres?.forEach { genre -> genres.add(genre.name) }
                } else {
                    tvShow?.genres?.forEach { genre -> genres.add(genre.name) }
                }

                val seasons = if ((tvShow?.numberOfSeasons ?: 0) > 1) "Seasons" else "Season"
                val episodes = if ((tvShow?.numberOfEpisodes ?: 0) > 1) "Episodes" else "Episode"

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    text = if (type == 1) {
                        "${AppUtil.formatReleaseYear(movie?.releaseDate)} • ${
                            TextUtils.join(
                                ", ",
                                movie?.originCountry ?: emptyList<String>()
                            )
                        } • ${
                            TextUtils.join(
                                ", ",
                                genres
                            )
                        } • ${AppUtil.formatRuntime(movie?.runtime ?: 0)}"
                    } else {
                        "${
                            AppUtil.formatReleaseYear(
                                tvShow?.firstAirDate,
                                tvShow?.lastAirDate
                            )
                        } • ${
                            TextUtils.join(
                                ", ",
                                tvShow?.originCountry ?: emptyList<String>()
                            )
                        } • ${
                            TextUtils.join(", ", genres)
                        }\n${tvShow?.numberOfSeasons} $seasons • ${tvShow?.numberOfEpisodes} $episodes • ${tvShow?.status}"
                    },
                    color = Color.White
                )

                if (type == 1) {
                    ReviewSection(
                        rating = movie?.voteAverage ?: 0.0,
                        reviewCount = movie?.voteCount ?: 0
                    ) {
                        onReviewClick(movie?.id ?: 0L, type)
                    }
                } else {
                    ReviewSection(
                        rating = tvShow?.voteAverage ?: 0.0,
                        reviewCount = tvShow?.voteCount ?: 0
                    ) {
                        onReviewClick(tvShow?.id ?: 0L, type)
                    }
                }

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = 20.dp),
                    text = "Plot Summary:",
                    color = Color.White
                )

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    text =
                        if (type == 1) {
                            movie?.overview.toString()
                        } else {
                            tvShow?.overview.toString()
                        },
                    color = Color.White
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularIconButton(
                icon = Icons.Default.ArrowBack,
                onClick = { navController.navigateUp() }
            )

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(CircleShape)
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        if (type == 1) {
                            if (movie?.accountStates?.watchlist == true) {
                                viewModel.addToWatchlist(type, movie?.id ?: 0L, false)
                            } else {
                                viewModel.addToWatchlist(type, movie?.id ?: 0L, true)
                            }
                        } else {
                            if (tvShow?.accountStates?.watchlist == true) {
                                viewModel.addToWatchlist(type, tvShow?.id ?: 0L, false)
                            } else {
                                viewModel.addToWatchlist(type, tvShow?.id ?: 0L, true)
                            }
                        }

                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(10.dp),
                    color = Color.White,
                    text = if (type == 1) {
                        if (movie?.accountStates?.watchlist == true) "Remove from Watchlist" else "Add to Watchlist"
                    } else {
                        if (tvShow?.accountStates?.watchlist == true) "Remove from Watchlist" else "Add to Watchlist"
                    },
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ReviewSection(
    rating: Double,
    reviewCount: Int,
    onReviewClick: () -> Unit
) {
    Surface(
        onClick = onReviewClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "User Reviews",
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    DynamicStarRating(rating = rating)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${rating.times(10.0).roundToInt() / 10.0}/10",
                        fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                        color = Color.White
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$reviewCount reviews",
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DynamicStarRating(
    rating: Double, // The 1.0 - 10.0 value
    starColor: Color = Color(0xFFFFD700) // Gold color
) {
    Row {
        // We loop 5 times to create 5 stars
        for (i in 1..5) {
            // Each star position represents a threshold of 2 points (2, 4, 6, 8, 10)
            val fullStarThreshold = i * 2.0
            // The half-way point for that specific star
            val halfStarThreshold = fullStarThreshold - 1.0

            val icon = when {
                rating >= fullStarThreshold -> Icons.Filled.Star
                rating >= halfStarThreshold -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}