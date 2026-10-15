package com.kira.android.cinemadb.features.details

import android.text.TextUtils
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.model.enums.MediaType
import com.kira.android.cinemadb.ui.component.CircularIconButton
import com.kira.android.cinemadb.ui.component.HeaderText
import com.kira.android.cinemadb.ui.component.ReviewSection
import com.kira.android.cinemadb.utils.AppUtil

@Composable
fun DetailsScreen(
    mediaId: Long,
    mediaType: String,
    onReviewClick: (Long, String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val detailsUiState by viewModel.detailsUiState.collectAsStateWithLifecycle()
    val isAddedToWatchlist =
        if (mediaType == MediaType.MOVIE.name)
            detailsUiState.movieWatchlistStatus
        else
            detailsUiState.tvShowWatchlistStatus

    LaunchedEffect(key1 = mediaId) {
        when (mediaType) {
            MediaType.MOVIE.name -> {
                viewModel.getMovieDetails(mediaId)
            }

            MediaType.TV_SHOW.name -> {
                viewModel.getTVShowDetails(mediaId)
            }
        }
        viewModel.getWatchlistStatus(mediaId, mediaType)
    }

    LaunchedEffect(key1 = true) {
        viewModel.detailsEffect.collect { effect ->
            when (effect) {
                is DetailsUiEffect.ShowSnackbar -> {

                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (mediaType == MediaType.MOVIE.name) {
            detailsUiState.movieDetails?.let { movie ->
                SetupDetails(
                    movie = movie,
                    tvShow = null,
                    mediaId,
                    mediaType,
                    onReviewClick,
                    onBackClick,
                    isAddedToWatchlist,
                    viewModel,
                )
            }
        } else {
            detailsUiState.tvShowDetails?.let { tvShow ->
                SetupDetails(
                    movie = null,
                    tvShow = tvShow,
                    mediaId,
                    mediaType,
                    onReviewClick,
                    onBackClick,
                    isAddedToWatchlist,
                    viewModel
                )
            }
        }

        if (detailsUiState.isLoading && detailsUiState.movieDetails == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun SetupDetails(
    movie: MovieResult? = null,
    tvShow: TVShowResult? = null,
    mediaId: Long,
    mediaType: String,
    onReviewClick: (mediaId: Long, type: String) -> Unit,
    onBackClick: () -> Unit,
    isAddedToWatchlist: Boolean,
    viewModel: DetailsViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp)
        ) {
            val posterPath = AppUtil.retrievePosterImageUrl(
                if (mediaType == MediaType.MOVIE.name) {
                    movie?.posterPath.toString()
                } else {
                    tvShow?.posterPath.toString()
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                AsyncImage(
                    model = posterPath,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(600.dp),
                    contentScale = ContentScale.Crop,
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
                        if (mediaType == MediaType.MOVIE.name) {
                            movie?.tagline?.ifBlank { movie.title }.toString()
                        } else {
                            tvShow?.tagline?.ifBlank { tvShow.name }.toString()
                        },
                    color = Color.White
                )

                HeaderText("Title")

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier
                        .wrapContentWidth(),
                    text = if (mediaType == MediaType.MOVIE.name) {
                        movie?.title.toString()
                    } else {
                        tvShow?.name.toString()
                    },
                    color = Color.White
                )

                HeaderText("Cast")

                val casts = ArrayList<String>()
                if (mediaType == MediaType.MOVIE.name) {
                    movie?.credits?.cast?.forEach { cast ->
                        if (casts.size < 7) cast.name?.let { casts.add(it) }
                    }
                } else {
                    tvShow?.credits?.cast?.forEach { cast ->
                        if (casts.size < 7) cast.name?.let { casts.add(it) }
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

                HeaderText("Details")

                val genres = ArrayList<String>()
                if (mediaType == MediaType.MOVIE.name) {
                    movie?.genres?.forEach { genre -> genre.name?.let { genres.add(it) } }
                } else {
                    tvShow?.genres?.forEach { genre -> genre.name?.let { genres.add(it) } }
                }

                val seasons = if ((tvShow?.numberOfSeasons ?: 0) > 1) "Seasons" else "Season"
                val episodes = if ((tvShow?.numberOfEpisodes ?: 0) > 1) "Episodes" else "Episode"

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier.fillMaxWidth(),
                    text = if (mediaType == MediaType.MOVIE.name) {
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

                if (mediaType == MediaType.MOVIE.name) {
                    ReviewSection(
                        rating = movie?.voteAverage ?: 0.0,
                        reviewCount = movie?.voteCount ?: 0
                    ) {
                        onReviewClick(movie?.id ?: 0L, mediaType)
                    }
                } else {
                    ReviewSection(
                        rating = tvShow?.voteAverage ?: 0.0,
                        reviewCount = tvShow?.voteCount ?: 0
                    ) {
                        onReviewClick(tvShow?.id ?: 0L, mediaType)
                    }
                }

                HeaderText("Plot Summary:")

                Text(
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    text =
                        if (mediaType == MediaType.MOVIE.name) {
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
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = { onBackClick() }
            )

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(CircleShape)
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        if (isAddedToWatchlist) {
                            viewModel.addToWatchlist(mediaType, mediaId, false)
                        } else {
                            viewModel.addToWatchlist(mediaType, mediaId, true)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(10.dp),
                    color = Color.White,
                    text = if (isAddedToWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    fontSize = 15.sp
                )
            }
        }
    }
}