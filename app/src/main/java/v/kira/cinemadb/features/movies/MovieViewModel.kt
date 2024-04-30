package v.kira.cinemadb.features.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase
): ViewModel() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    private val mutableMovieState: MutableSharedFlow<MovieState> = MutableSharedFlow()
    val movieState = mutableMovieState.asSharedFlow()

    init {
        viewModelScope.launch(
            CoroutineExceptionHandler {_, error ->
                runBlocking {
                    mutableMovieState.emit(MovieState.ShowError(error))
                }
            }
        ) {
            val popularMovies = useCase.getPopularMovies(header, language, 1)
            mutableMovieState.emit(MovieState.SetPopularMovies(popularMovies))
        }
    }
}