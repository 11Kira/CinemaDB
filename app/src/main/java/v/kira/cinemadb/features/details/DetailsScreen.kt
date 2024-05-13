package v.kira.cinemadb.features.details

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow

lateinit var viewModel: DetailsViewModel

@Composable
fun DetailsScreen (
    movieId: Long,
    movieImage: String
) {
    viewModel = hiltViewModel()
    MainScreen(viewModel.detailsState)
    viewModel.getMovieDetails(movieId)

    /*Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(movieImage).crossfade(true).build(),
            contentDescription = "Description",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movieId.toString())
    }*/

}

@Composable
fun MainScreen(sharedFlow: SharedFlow<DetailsState>) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is DetailsState.SetMovieDetails -> {
                        Log.e("details", state.movieDetails.toString())
                    }
                    is DetailsState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }
                }
            }
        }
    }
}