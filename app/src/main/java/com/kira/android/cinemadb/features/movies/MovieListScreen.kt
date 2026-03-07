package com.kira.android.cinemadb.features.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.MainActivity.Companion.TOP_RATED
import com.kira.android.cinemadb.MainActivity.Companion.TRENDING
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.components.SearchTextField
import com.kira.android.cinemadb.components.SegmentedControl
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.util.AppUtil
import kotlin.math.roundToInt

lateinit var viewModel: MovieViewModel
private var currentlySelected = 0

@Composable
fun MovieListScreen(
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit,
    onScroll: (Float, Float) -> Unit
) {
    viewModel = hiltViewModel()
    MainMovieScreen(
        contentPadding,
        onItemClick,
        onScroll
    )
}

@Composable
fun MainMovieScreen(
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit,
    onScroll: (Float, Float) -> Unit
) {
    val movies by rememberUpdatedState(newValue = viewModel.moviesPagingState.collectAsLazyPagingItems())
    val categoryList = listOf("Trending", "Top Rated")
    val focusManager = LocalFocusManager.current

    val toolbarHeight = 145.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val topBarOffsetPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newTopOffset = topBarOffsetPx.floatValue + delta
                topBarOffsetPx.floatValue = newTopOffset.coerceIn(-toolbarHeightPx, 0f)
                onScroll(delta, -toolbarHeightPx)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        PopulateMovieGrid(movies, contentPadding, onItemClick)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, topBarOffsetPx.floatValue.roundToInt()) }
                .background(Color.Black)
                .statusBarsPadding()
        ) {
            MovieSegmentedControl(categoryList.toList()) { selectedItem ->
                when (selectedItem) {
                    0 -> {
                        if (currentlySelected != 0) {
                            viewModel.getMovies(TRENDING)
                            currentlySelected = 0
                        }
                        viewModel.updateScrollToTopState(true)
                        focusManager.clearFocus()
                    }

                    1 -> {
                        if (currentlySelected != 1) {
                            viewModel.getMovies(TOP_RATED)
                            currentlySelected = 1
                        }
                        viewModel.updateScrollToTopState(true)
                        focusManager.clearFocus()
                    }
                }
            }
            SearchField(
                onClearFocus = { focusManager.clearFocus() }
            )
        }
    }
}

@Composable
fun MovieSegmentedControl(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedTab by remember { mutableStateOf(viewModel.selectedMovieTab) }

    SegmentedControl(
        items,
        selectedTab,
        onClick = { item ->
            viewModel.updateSelectedMovieTab(item)
            viewModel.clearSearch()
        },
        onItemSelection
    )
}

@Composable
fun SearchField(
    onClearFocus: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val searchText by viewModel.searchText.collectAsState()
    val onSearchTextChange = viewModel::onSearchMovieTextChange
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
    ) {
        SearchTextField(
            searchText,
            onSearchTextChange,
            onClearClicked = {
                onSearchTextChange("")
                viewModel.clearSearch()
                viewModel.updateScrollToTopState(true)
                onClearFocus()
            },
            keyboardController,
            focusManager
        )
    }
}

@Composable
fun PopulateMovieGrid(
    movies: LazyPagingItems<MovieResult>,
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit
) {
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
        LazyVerticalStaggeredGrid(
            state = lazyRowState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = 150.dp,
                bottom = contentPadding.calculateBottomPadding()
            ),
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
                                .heightIn(
                                    min = 250.dp,
                                    max = 320.dp
                                ) // Limits the "chaos" of the stagger
                                .clickable { selectedMovie?.id?.let { onItemClick(it, 1) } },
                            model = posterPath,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Description",
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
                                text = (selectedMovie?.voteAverage?.times(10.0)?.roundToInt()?.div(10.0)).toString(),
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