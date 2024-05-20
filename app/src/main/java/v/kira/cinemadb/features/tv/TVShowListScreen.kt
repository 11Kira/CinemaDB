package v.kira.cinemadb.features.tv

import android.util.Log
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.MainActivity
import v.kira.cinemadb.model.TVShowResult
import v.kira.cinemadb.util.AppUtil

lateinit var viewModel: TVViewModel

@Composable
fun TVShowListScreen(
    onItemClick: (Long, Int) -> Unit
) {
    viewModel = hiltViewModel()
    MainScreen(viewModel.tvShowState, onItemClick)
    viewModel.getTVShowList(MainActivity.TRENDING, 1)
}

@Composable
fun MainScreen(sharedFlow: SharedFlow<TVShowState>, onItemClick: (Long, Int) -> Unit) {
    val movieList = remember { mutableStateListOf<TVShowResult>() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is TVShowState.SetTrendingTVShows -> {
                        movieList.clear()
                        movieList.addAll(state.trendingTVShows)
                    }

                    is TVShowState.SetAiringTodayTVShows -> {
                        movieList.clear()
                        movieList.addAll(state.airingTodayTVShows)
                    }

                    is TVShowState.SetTopRatedTVShows -> {
                        movieList.clear()
                        movieList.addAll(state.topRatedTVShows)
                    }

                    is TVShowState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }

    val categoryList = listOf("Trending", "Airing Today", "Top Rated")
    Column {
        SegmentedControl(categoryList.toList()) { selectedItem ->
            when (selectedItem) {
                0 -> { viewModel.getTVShowList(MainActivity.TRENDING, 1) }
                1 -> { viewModel.getTVShowList(MainActivity.NOW_PLAYING, 1) }
                else -> { viewModel.getTVShowList(MainActivity.TOP_RATED, 1) }
            }
        }
        PopulateGrid(movieList, onItemClick)
    }
}

@Composable
fun SegmentedControl(
    items: List<String>,
    onItemSelection: (selectedItemIndex: Int) -> Unit
) {
    val selectedIndex = remember { mutableStateOf(0) }
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
                    },
                    colors = if (selectedIndex.value == index) {
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
                    border = BorderStroke(
                        1.5.dp, Color.DarkGray
                    ),
                ) {
                    Text(
                        text = item,
                        style = LocalTextStyle.current.copy(
                            fontSize = 12.sp,
                            fontWeight = if (selectedIndex.value == index)
                                LocalTextStyle.current.fontWeight
                            else
                                FontWeight.Normal,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Composable
fun PopulateGrid(
    tvShows: List<TVShowResult>,
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
                items(tvShows) { tvShow ->
                    val posterPath = AppUtil.retrievePosterImageUrl(tvShow.posterPath)
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clickable { onItemClick(tvShow.id, 2) },
                        model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                        contentDescription = "Description",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        )
    }
}