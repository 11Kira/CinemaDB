package com.kira.android.cinemadb.features.account.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.components.SegmentedControl
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.util.AppUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

lateinit var viewModel: WatchlistViewModel
private var currentlySelected = 0

@Composable
fun WatchlistScreen(
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit
) {
    viewModel = hiltViewModel()
    MainWatchlistScreen(contentPadding, onItemClick)
}

@Composable
fun MainWatchlistScreen(contentPadding: PaddingValues, onItemClick: (Long, Int) -> Unit) {
    val movieWatchlist by rememberUpdatedState(newValue = viewModel.movieWatchlistState.collectAsLazyPagingItems())
    val tvShowWatchlist by rememberUpdatedState(newValue = viewModel.tvShowWatchlistState.collectAsLazyPagingItems())
    val typeSelected  by remember { mutableStateOf(viewModel.selectedWatchlistTab) }
    val typeList = listOf("Movies", "TV Shows")
    LaunchedEffect(Unit) {
        if (currentlySelected == 0) {
            viewModel.getMovieWatchlist()
        } else {
            viewModel.getTVShowWatchlist()
        }
    }
    Column {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        SegmentedControlWatchlist(typeList.toList()) { selectedItem ->
            when (selectedItem) {
                0 -> {
                    if (currentlySelected != 0) {
                        viewModel.getMovieWatchlist()
                        currentlySelected = 0
                    }
                    viewModel.updateScrollToTopState(true)
                }
                1 -> {
                    if (currentlySelected != 1) {
                        viewModel.getTVShowWatchlist()
                        currentlySelected = 1
                    }
                    viewModel.updateScrollToTopState(true)
                }
            }
        }

        if (typeSelected.collectAsState().value == "Movies") {
            PopulateWatchlistGrid(movieWatchlist, null, 1, contentPadding, onItemClick)
        } else {
            PopulateWatchlistGrid(null, tvShowWatchlist, 2, contentPadding, onItemClick)
        }
    }
}

@Composable
fun SegmentedControlWatchlist(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedTab by remember { mutableStateOf(viewModel.selectedWatchlistTab) }

    SegmentedControl(
        items,
        selectedTab,
        onClick = { item ->
            viewModel.updateSelectedWatchlistTab(item)
        },
        onItemSelection
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopulateWatchlistGrid(
    movies: LazyPagingItems<MovieResult>? = null,
    tvShows: LazyPagingItems<TVShowResult>? = null,
    type: Int,
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            if (type == 1) viewModel.getMovieWatchlist() else viewModel.getTVShowWatchlist()
            delay(500)
            isRefreshing = false
        }
    }
    val lazyRowState = rememberLazyStaggeredGridState()
    if (viewModel.scrollToTopState.collectAsState().value) {
        LaunchedEffect(key1 = true) {
            lazyRowState.scrollToItem(0)
            viewModel.updateScrollToTopState(false)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        PullToRefreshBox(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyVerticalStaggeredGrid(
                state = lazyRowState,
                contentPadding = PaddingValues(
                    bottom = contentPadding.calculateBottomPadding() + 16.dp
                ),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 5.dp,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                content = {
                    if (type == 1) {
                        items(movies?.itemCount ?: 0) { index ->
                            val selectedMovie = movies?.get(index)
                            GridItemContent(
                                selectedMovie = selectedMovie,
                                selectedTvShow = null,
                                type = 1,
                                onItemClick = { id, type ->
                                    onItemClick(id, type)
                                }
                            )
                        }
                    } else {
                        items(tvShows?.itemCount ?: 0) { index ->
                            val selectedTvShow = tvShows?.get(index)
                            GridItemContent(
                                selectedMovie = null,
                                selectedTvShow = selectedTvShow,
                                type = 2,
                                onItemClick = { id, type ->
                                    onItemClick(id, type)
                                }
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun GridItemContent(
    selectedMovie: MovieResult? = null,
    selectedTvShow: TVShowResult? = null,
    type: Int,
    onItemClick: (Long, Int) -> Unit
) {
    val posterPath =
        if (type == 1) selectedMovie?.posterPath?.let { AppUtil.retrievePosterImageUrl(it) } else selectedTvShow?.posterPath?.let {
            AppUtil.retrievePosterImageUrl(
                it
            )
        }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clickable {
                    if (type == 1) selectedMovie?.id?.let {
                        onItemClick(
                            it,
                            1
                        )
                    } else selectedTvShow?.id?.let { onItemClick(it, 2) }
                },
            model = posterPath,
            contentDescription = "Description",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_video),
            error = painterResource(id = R.drawable.ic_video)
        )

        Box(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopEnd)
                .size(35.dp)
                .clip(CircleShape)
                .background(color = Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.wrapContentHeight(),
                color = Color.White,
                text = if (type == 1) {
                    selectedMovie?.voteAverage?.times(10.0)?.roundToInt()?.div(10.0).toString()
                } else {
                    selectedTvShow?.voteAverage?.times(10.0)?.roundToInt()?.div(10.0).toString()
                },
                fontFamily = Font(R.font.roboto_bold).toFontFamily(),
                fontSize = 15.sp
            )
        }
    }
}