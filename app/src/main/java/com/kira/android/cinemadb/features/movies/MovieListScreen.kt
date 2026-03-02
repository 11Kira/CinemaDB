package com.kira.android.cinemadb.features.movies

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.kira.android.cinemadb.MainActivity.Companion.TOP_RATED
import com.kira.android.cinemadb.MainActivity.Companion.TRENDING
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.util.AppUtil
import kotlin.math.roundToInt

lateinit var viewModel: MovieViewModel
private var currentlySelected = 0

@Composable
fun MovieListScreen(
    contentPadding: PaddingValues,
    onItemClick: (Long, Int) -> Unit
) {
    viewModel = hiltViewModel()
    MainMovieScreen(contentPadding, onItemClick)
}

@Composable
fun MainMovieScreen(contentPadding: PaddingValues, onItemClick: (Long, Int) -> Unit) {
    val movies by rememberUpdatedState(newValue = viewModel.moviesPagingState.collectAsLazyPagingItems())
    val categoryList = listOf("Trending", "Top Rated")
    val focusManager = LocalFocusManager.current
    Column {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

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
        PopulateMovieGrid(movies, contentPadding, onItemClick)
    }
}

@Composable
fun MovieSegmentedControl(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedTab by remember { mutableStateOf(viewModel.selectedMovieTab) }

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
                        viewModel.updateSelectedMovieTab(item)
                        viewModel.clearSearch()
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
                    border = if (selectedTab.collectAsState().value == item) {
                        BorderStroke(1.5.dp, Color.White)
                    } else {
                        BorderStroke(1.5.dp, Color.DarkGray)
                    }
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
fun SearchField(
    onClearFocus: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val searchText by viewModel.searchText.collectAsState()
    val onSearchTextChange = viewModel::onSearchMovieTextChange
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

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
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 10.dp)
                .onFocusChanged { isFocused = it.isFocused },
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(38.dp)
                        .background(color = Color.Transparent)
                        .border(
                            width = 1.5.dp,
                            color = if (isFocused) Color.White else Color.DarkGray,
                            shape = RoundedCornerShape(24)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = if (isFocused) Color.White else Color.Gray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchText.isEmpty()) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterStart),
                                text = "Search Movie...",
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                                    fontSize = 14.sp,
                                ),
                            )
                        }
                        innerTextField()
                    }
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onSearchTextChange("")
                                viewModel.clearSearch()
                                viewModel.updateScrollToTopState(true)
                                onClearFocus()
                            },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
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
            contentPadding = PaddingValues(
                bottom = contentPadding.calculateBottomPadding() + 16.dp
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
                                .height(350.dp)
                                .clickable { selectedMovie?.id?.let { onItemClick(it, 1) } },
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
                                .background(color = Color.DarkGray),
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