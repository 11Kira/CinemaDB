package v.kira.cinemadb.features.account.watchlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.first
import v.kira.cinemadb.R
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
import v.kira.cinemadb.util.AppUtil
import kotlin.math.roundToInt

lateinit var viewModel: WatchlistViewModel
private var currentlySelected = 0

@Composable
fun WatchlistScreen(
    onItemClick: (Long, Int) -> Unit
) {
    viewModel = hiltViewModel()
    MainWatchlistScreen(onItemClick)
    viewModel.updateScrollToTopState(true)
}

@Composable
fun MainWatchlistScreen(onItemClick: (Long, Int) -> Unit) {
    val movieWatchlist by rememberUpdatedState(newValue = viewModel.movieWatchlistState.collectAsLazyPagingItems())
    val tvShowWatchlist by rememberUpdatedState(newValue = viewModel.tvShowWatchlistState.collectAsLazyPagingItems())
    val typeSelected  by remember { mutableStateOf(viewModel.selectedWatchlistTab) }
    val typeList = listOf("Movies", "TV Shows")
    Column {
        SegmentedControlWatchlist(typeList.toList()) { selectedItem ->
            when (selectedItem) {
                0 -> {
                    if (currentlySelected != 0) {
                        viewModel.getMovieWatchlist()
                        currentlySelected = 0
                    }
                }
                1 -> {
                    if (currentlySelected != 1) {
                        viewModel.getTVShowWatchlist()
                        currentlySelected = 1
                    }
                }
            }
            viewModel.updateScrollToTopState(true)
        }

        if (typeSelected.collectAsState().value == "Movies") {
            PopulateMovieWatchlistGrid(movieWatchlist, onItemClick)
        } else {
            PopulateTVShowWatchlistGrid(tvShowWatchlist, onItemClick)
        }
    }
}

@Composable
fun SegmentedControlWatchlist(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedTab by remember { mutableStateOf(viewModel.selectedWatchlistTab) }

    Box(modifier = Modifier.background(Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            items.forEachIndexed { index, item ->
                OutlinedButton(
                    modifier = when (index) {
                        0 -> {
                            Modifier
                                .fillMaxWidth()
                                .weight(2f)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        } else -> {
                            Modifier
                                .fillMaxWidth()
                                .weight(2f)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    },
                    onClick = {
                        selectedIndex.value = index
                        onItemSelection(selectedIndex.value)
                        viewModel.updateSelectedWatchlistTab(item)
                    },
                    colors = if (selectedTab.collectAsState().value == item) {
                        ButtonDefaults.outlinedButtonColors(backgroundColor = Color.DarkGray)
                    } else {
                        ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    },
                    shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStartPercent = 24,
                            topEndPercent = 0,
                            bottomStartPercent = 24,
                            bottomEndPercent = 0
                        )

                        items.size - 1 -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 24,
                            bottomStartPercent = 0,
                            bottomEndPercent = 24
                        )

                        else -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 0,
                            bottomStartPercent = 0,
                            bottomEndPercent = 0
                        )
                    },
                    border = BorderStroke(1.5.dp, Color.DarkGray),
                ) {
                    Text(
                        text = item,
                        style = LocalTextStyle.current.copy(
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = if (selectedTab.collectAsState().value == item) Font(
                                R.font.roboto_bold).toFontFamily() else Font(R.font.roboto_medium).toFontFamily(),
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PopulateMovieWatchlistGrid(
    movies: LazyPagingItems<MovieResult>,
    onItemClick: (Long, Int) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        val scrollToTop by rememberUpdatedState(viewModel.scrollToTopState)
        val lazyRowState = rememberLazyStaggeredGridState()

        LaunchedEffect(key1 = movies.itemCount) {
            if (scrollToTop.first()) {
                lazyRowState.scrollToItem(0)
                viewModel.updateScrollToTopState(false)
            }
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(movies.itemCount) {  index ->
                    val selectedMovie = movies[index]
                    val posterPath = selectedMovie?.posterPath?.let { AppUtil.retrievePosterImageUrl(it) }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clickable { selectedMovie?.id?.let { onItemClick(it, 1) } },
                            model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                            contentDescription = "Description",
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.TopEnd)
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(color = Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.wrapContentHeight(),
                                color = Color.White,
                                text = (selectedMovie?.voteAverage?.times(10.0)?.roundToInt()?.div(10.0)).toString(),
                                fontFamily = Font(R.font.roboto_bold).toFontFamily(),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun PopulateTVShowWatchlistGrid(
    tvShows: LazyPagingItems<TVShowResult>,
    onItemClick: (Long, Int) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        val scrollToTop by rememberUpdatedState(viewModel.scrollToTopState)
        val lazyRowState = rememberLazyStaggeredGridState()

        LaunchedEffect(key1 = tvShows.itemCount) {
            if (scrollToTop.first()) {
                lazyRowState.scrollToItem(0)
                viewModel.updateScrollToTopState(false)
            }
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(tvShows.itemCount) {  index ->
                    val selectedTVShow = tvShows[index]
                    val posterPath = selectedTVShow?.posterPath?.let { AppUtil.retrievePosterImageUrl(it) }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clickable { selectedTVShow?.id?.let { onItemClick(it, 2) } },
                            model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                            contentDescription = "Description",
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.TopEnd)
                                .size(35.dp)
                                .clip(CircleShape)
                                .background(color = Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.wrapContentHeight(),
                                color = Color.White,
                                text = (selectedTVShow?.voteAverage?.times(10.0)?.roundToInt()?.div(10.0)).toString(),
                                fontSize = 15.sp,
                                fontFamily = Font(R.font.roboto_bold).toFontFamily()
                            )
                        }
                    }
                }
            },
        )
    }
}