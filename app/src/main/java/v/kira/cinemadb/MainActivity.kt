package v.kira.cinemadb

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.features.movies.MovieState
import v.kira.cinemadb.features.movies.MovieViewModel
import v.kira.cinemadb.model.CinemaResult
import v.kira.cinemadb.ui.theme.CinemaDBTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaDBTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    SetupViewModel(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SetupViewModel(viewModel: MovieViewModel) {
    MainScreen(viewModel.movieState)
    viewModel.getPopular()
}


@Composable
fun MainScreen(sharedFlow: SharedFlow<MovieState>) {
    val messages = remember { mutableStateListOf<CinemaResult>() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { state ->
                when (state) {
                    is MovieState.SetPopularMovies -> {
                        state.popularMovies.forEach{
                            Log.e("Popular Movie: ", it.title)
                        }
                    }

                    is MovieState.ShowError -> {
                        Log.e("Error: ", state.error.toString())
                    }

                    else -> {
                        Log.e("Error: ", state.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CinemaDBTheme {
        Greeting("Android")
    }
}