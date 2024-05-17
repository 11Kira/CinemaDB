package v.kira.cinemadb.features.details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult

lateinit var viewModel: DetailsViewModel

@Composable
fun DetailsScreen (
    id: Long,
    type: Int
) {
    viewModel = hiltViewModel()
    MainScreen(viewModel.movieState)
    when(type) {
        1 -> { viewModel.getMovieDetails(id) }
        2 -> { viewModel.getTVShowDetails(id) }
    }
}

@Composable
fun MainScreen(sharedFlow: SharedFlow<DetailsState>) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedMovie by remember { mutableStateOf<MovieResult?>(null) }
    var selectedTVShow by remember { mutableStateOf<TVShowResult?>(null) }

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is DetailsState.SetMovieDetails -> {
                        selectedMovie = state.movieDetails
                    }

                    is DetailsState.SetTvShowDetails -> {
                        selectedTVShow = state.tvShowDetails
                    }
                    is DetailsState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }

    selectedMovie?.let {
        SetupMovieDetails(movie = it)
    }

    selectedTVShow?.let {
        SetupTVShowDetails(tvShow = it)
    }
}

@Composable
fun SetupMovieDetails(movie: MovieResult) {
    Column(
    modifier = Modifier
        .fillMaxSize()
) {
        val posterPath = "https://image.tmdb.org/t/p/original/"+movie.posterPath

        AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
        contentDescription = "Poster",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title)
    }
}

@Composable
fun SetupTVShowDetails(tvShow: TVShowResult) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val posterPath = "https://image.tmdb.org/t/p/original/"+tvShow.posterPath

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
            contentDescription = "Poster",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = tvShow.name)
    }
}