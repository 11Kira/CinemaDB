package com.kira.android.cinemadb.features.search

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.util.AppUtil
import kotlin.math.roundToInt

lateinit var viewModel: SearchViewModel
private var currentlySelected = 0

@Composable
fun SearchScreen(
    onItemClick: (Long, Int) -> Unit
) {
    viewModel = hiltViewModel()
    MainSearchScreen(onItemClick)
}

@Composable
fun MainSearchScreen(onItemClick: (Long, Int) -> Unit) {
    val movies by rememberUpdatedState(newValue = viewModel.movieSearchState.collectAsLazyPagingItems())
    val tvShows by rememberUpdatedState(newValue = viewModel.tvShowSearchState.collectAsLazyPagingItems())
    val typeSelected by remember { mutableStateOf(viewModel.selectedSearchTab) }
    val typeList = listOf("Movies", "TV Shows")
    Column {
        SegmentedControlSearch(typeList.toList()) { selectedItem ->
            when(selectedItem) {
                0 -> {
                    if (currentlySelected != 0) {
                        currentlySelected = 0
                    }
                    viewModel.updateScrollToTopState(true)
                }
                1 -> {
                    if (currentlySelected != 1) {
                        currentlySelected = 1
                    }
                    viewModel.updateScrollToTopState(true)
                }
            }
        }
        SearchField(currentlySelected)
        if (typeSelected.collectAsState().value == "Movies") {
            PopulateMovieSearchGrid(movies, onItemClick)
        } else {
            PopulateTVShowSearchGrid(tvShows, onItemClick)
        }
    }
}

@Composable
fun SegmentedControlSearch(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedTab by remember { mutableStateOf(viewModel.selectedSearchTab) }

    Box(modifier = Modifier.background(Color.Black)) {
        Row (
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
                        if (viewModel.searchText.value.isNotEmpty()) {
                            if (index == 0) {
                                viewModel.clearSearch()
                                viewModel.onSearchMovieTextChange(viewModel.searchText.value)
                            } else {
                                viewModel.clearSearch()
                                viewModel.onSearchTVShowTextChange(viewModel.searchText.value)
                            }
                        }
                        viewModel.updateSelectedSearchTab(item)
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

                        else -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 24,
                            bottomStartPercent = 0,
                            bottomEndPercent = 24
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
fun SearchField(typeSelected: Int) {
    val searchText by viewModel.searchText.collectAsState()
    val onSearchTextChange = if (typeSelected == 0) viewModel::onSearchMovieTextChange else viewModel::onSearchTVShowTextChange
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 10.dp),
            shape = RoundedCornerShape(24),
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier.clickable {
                        onSearchTextChange("")
                        viewModel.clearSearch()
                        viewModel.updateScrollToTopState(true)
                    })
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
            ),
            placeholder = {
                Text(
                    text = if (typeSelected == 0) "Search Movie" else "Search TV Show",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = Font(R.font.roboto_medium).toFontFamily()
                    ),
                )
            },
        )
    }

}

@Composable
fun PopulateMovieSearchGrid(
    movies: LazyPagingItems<MovieResult>,
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
                            placeholder = painterResource(id = R.drawable.ic_video),
                            error = painterResource(id = R.drawable.ic_video)
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
fun PopulateTVShowSearchGrid(
    tvShows: LazyPagingItems<TVShowResult>,
    onItemClick: (Long, Int) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
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
                            placeholder = painterResource(id = R.drawable.ic_video),
                            error = painterResource(id = R.drawable.ic_video)
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