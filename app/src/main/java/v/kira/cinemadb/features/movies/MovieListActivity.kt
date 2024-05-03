package v.kira.cinemadb.features.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import v.kira.cinemadb.ui.theme.CinemaDBTheme

@AndroidEntryPoint
class MovieListActivity : ComponentActivity() {

    //private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //SetupViewModel(viewModel = viewModel)
                }
            }
        }
    }

/*    companion object {
        const val POPULAR = 1
        const val NOW_PLAYING = 2
        const val TOP_RATED = 3
    }*/
}

/*
@Composable
fun SetupViewModel(viewModel: MovieViewModel) {
    MainScreen(viewModel.movieState)
    viewModel.getMovieList(POPULAR, 1)
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
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                )
            }
        },

        )
}*/
