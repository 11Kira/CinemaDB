package v.kira.cinemadb.features.details

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.SharedFlow
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
import v.kira.cinemadb.util.AppUtil
import kotlin.math.roundToInt

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

    selectedMovie?.let { SetupMovieDetails(movie = it) }
    selectedTVShow?.let { SetupTVShowDetails(tvShow = it) }
}

@Composable
fun SetupMovieDetails(movie: MovieResult) {
    Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .verticalScroll(rememberScrollState())
    ) {
        val posterPath = AppUtil.retrievePosterImageUrl(movie.posterPath)
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                contentDescription = "Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color = Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight(),
                    color = Color.White,
                    text = (movie.voteAverage.times(10.0).roundToInt() / 10.0).toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, start = 10.dp, bottom = 10.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                text = movie.tagline,
                color = Color.White
            )
            Text(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = movie.overview,
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 20.dp),
                text = "Origin Country:",
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                text = TextUtils.join(", ",movie.originCountry),
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 20.dp),
                text = "Genres:",
                color = Color.White
            )

            val genres = ArrayList<String>()
            movie.genres.forEach { genre -> genres.add(genre.name) }

            Text(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth(),
                text = TextUtils.join(", ",genres),
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 20.dp),
                text = "Runtime:",
                color = Color.White
            )


            Text(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                text = movie.runtime.toString(),
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 20.dp),
                text = "Release date:",
                color = Color.White
            )

            Text(
                textAlign = TextAlign.Start,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
                text = movie.releaseDate,
                color = Color.White
            )
        }
    }
}

@Composable
fun SetupTVShowDetails(tvShow: TVShowResult) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        val posterPath = AppUtil.retrievePosterImageUrl(tvShow.posterPath)
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(posterPath).crossfade(true).build(),
                contentDescription = "Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(color = Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight(),
                    color = Color.White,
                    text = (tvShow.voteAverage.times(10.0).roundToInt() / 10.0).toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, start = 10.dp, bottom = 10.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                text = tvShow.tagline,
                color = Color.White
            )
            Text(
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                text = tvShow.overview,
                color = Color.White
            )
        }
    }
}