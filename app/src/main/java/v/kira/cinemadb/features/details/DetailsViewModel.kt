package v.kira.cinemadb.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import v.kira.cinemadb.features.movies.MovieUseCase
import v.kira.cinemadb.features.tv.TVUseCase
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val tvShowUseCase: TVUseCase
): ViewModel() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    private val mutableDetailsState: MutableSharedFlow<DetailsState> = MutableSharedFlow()
    val movieState = mutableDetailsState.asSharedFlow()

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = movieUseCase.getMovieDetails(header, movieId, language)
            mutableDetailsState.emit(DetailsState.SetMovieDetails(result))
        }
    }

    fun getTVShowDetails(tvSeriesId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = tvShowUseCase.getTVShowDetails(header, tvSeriesId, language)
            mutableDetailsState.emit(DetailsState.SetTvShowDetails(result))
        }
    }
}