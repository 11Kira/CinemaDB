package v.kira.cinemadb.features.movies

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.colorResource
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
import v.kira.cinemadb.MainActivity.Companion.NOW_PLAYING
import v.kira.cinemadb.MainActivity.Companion.POPULAR
import v.kira.cinemadb.MainActivity.Companion.TOP_RATED
import v.kira.cinemadb.R
import v.kira.cinemadb.model.CinemaResult

lateinit var viewModel: MovieViewModel

@Composable
fun MovieListScreen() {
    viewModel = hiltViewModel()
    MainScreen(viewModel.movieState)
    viewModel.getMovieList(POPULAR, 1)
}

@Composable
fun MainScreen(sharedFlow: SharedFlow<MovieState>) {
    val movieList = remember { mutableStateListOf<CinemaResult>() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is MovieState.SetPopularMovies -> {
                        movieList.clear()
                        movieList.addAll(state.popularMovies)
                    }

                    is MovieState.SetNowPlayingMovies -> {
                        movieList.clear()
                        movieList.addAll(state.nowPlayingMovies)
                    }

                    is MovieState.SetTopRatedMovies -> {
                        movieList.clear()
                        movieList.addAll(state.topRatedMovies)
                    }

                    is MovieState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }

    val categoryList = listOf("Popular", "Now Playing", "Top Rated")
    Column {
        SegmentedControl(categoryList.toList()) { selectedItem ->
            when (selectedItem) {
                0 -> { viewModel.getMovieList(POPULAR, 1) }
                1 -> { viewModel.getMovieList(NOW_PLAYING, 1) }
                else -> { viewModel.getMovieList(TOP_RATED, 1) }
            }
        }
        PopulateGrid(movieList)
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
                                .weight(3f)
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        } else -> {
                            Modifier
                                .fillMaxWidth()
                                .weight(3f)
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    },
                    onClick = {
                        selectedIndex.value = index
                        onItemSelection(selectedIndex.value)
                    },
                    colors = if (selectedIndex.value == index) {
                        ButtonDefaults.outlinedButtonColors(backgroundColor = colorResource(id = R.color.teal_200))
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
                        1.dp, if (selectedIndex.value == index) {
                            colorResource(id = R.color.teal_200)
                        } else {
                            colorResource(id = R.color.teal_200).copy(alpha = 0.75f)
                        }
                    ),
                ) {
                    Text(
                        text = item,
                        style = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            fontWeight = if (selectedIndex.value == index)
                                LocalTextStyle.current.fontWeight
                            else
                                FontWeight.Normal,
                            color = if (selectedIndex.value == index) {
                                Color.White
                            } else {
                                colorResource(id = R.color.teal_200).copy(alpha = 0.9f)
                            },
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Composable
fun PopulateGrid(movies: List<CinemaResult>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(movies) { movie ->
                val posterPath = "https://image.tmdb.org/t/p/original/"+movie.posterPath
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                    contentDescription = "Description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    )
}