package v.kira.cinemadb.features.movies

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.model.CinemaResult


@Composable
fun MovieListScreen(movieListType: Int) {
    val viewModel: MovieViewModel = hiltViewModel()
    MainScreen(viewModel.movieState)
    viewModel.getMovieList(movieListType, 1)
    //viewModel.getMovieList(NOW_PLAYING, 1)
    //viewModel.getMovieList(TOP_RATED, 1)
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
                        movieList.addAll(state.popularMovies)
                    }

                    is MovieState.SetNowPlayingMovies -> {
                        movieList.addAll(state.nowPlayingMovies)
                    }

                    is MovieState.SetTopRatedMovies -> {
                        movieList.addAll(state.topRatedMovies)
                    }

                    is MovieState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }

    PopulateGrid(movieList)
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