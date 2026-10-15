package com.kira.android.cinemadb.features.list.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.android.cinemadb.MainActivity.Companion.TOP_RATED
import com.kira.android.cinemadb.MainActivity.Companion.TRENDING
import com.kira.android.cinemadb.model.enums.MediaType
import com.kira.android.cinemadb.ui.component.MediaGrid
import com.kira.android.cinemadb.ui.component.SearchTextField
import com.kira.android.cinemadb.ui.component.SegmentedControl
import kotlin.math.roundToInt

private var currentlySelected = 0

@Composable
fun MovieListScreen(
    onItemClick: (Long, String) -> Unit,
    onScroll: (Float, Float) -> Unit,
    viewModel: MovieViewModel = hiltViewModel()
) {
    MainMovieScreen(
        onItemClick,
        onScroll,
        viewModel
    )
}

@Composable
fun MainMovieScreen(
    onItemClick: (Long, String) -> Unit,
    onScroll: (Float, Float) -> Unit,
    viewModel: MovieViewModel
) {
    val movies by rememberUpdatedState(newValue = viewModel.moviesPagingState.collectAsLazyPagingItems())
    val categoryList = listOf("Trending", "Top Rated")
    val focusManager = LocalFocusManager.current

    val searchText by viewModel.searchText.collectAsState()
    val onSearchTextChange = viewModel::onSearchMovieTextChange
    val keyboardController = LocalSoftwareKeyboardController.current

    val selectedTab by remember { mutableStateOf(viewModel.selectedMovieTab) }

    val toolbarHeight = 142.dp
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
        MediaGrid(
            movies = movies,
            tvShows = null,
            MediaType.MOVIE.name,
            onItemClick,
            viewModel
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, topBarOffsetPx.floatValue.roundToInt()) }
                .background(Color.Black)
                .statusBarsPadding()
        ) {


            SegmentedControl(
                categoryList.toList(),
                selectedTab,
                onClick = { item ->
                    viewModel.updateSelectedMovieTab(item)
                    viewModel.clearSearch()
                },
                { selectedItem ->
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
            )

            SearchTextField(
                searchText,
                onSearchTextChange,
                onClearClicked = {
                    onSearchTextChange("")
                    viewModel.clearSearch()
                    viewModel.updateScrollToTopState(true)
                    focusManager.clearFocus()
                },
                keyboardController,
                focusManager
            )
        }
    }
}