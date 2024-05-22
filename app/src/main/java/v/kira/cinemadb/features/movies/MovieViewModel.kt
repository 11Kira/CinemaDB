package v.kira.cinemadb.features.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import v.kira.cinemadb.MainActivity.Companion.NOW_PLAYING
import v.kira.cinemadb.MainActivity.Companion.TOP_RATED
import v.kira.cinemadb.MainActivity.Companion.TRENDING
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase: MovieUseCase,
): ViewModel() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    private val mutableMovieState: MutableSharedFlow<MovieState> = MutableSharedFlow()
    val movieState = mutableMovieState.asSharedFlow()

    private val _uiState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(PagingData.empty())
    val uiState: StateFlow<PagingData<MovieResult>> = _uiState.asStateFlow()

    fun getMovies(type: Int) {
        viewModelScope.launch {
            try {
                when (type) {
                    TRENDING -> {
                        useCase.getTrendingMovies(
                            header,
                            language,
                            this@MovieViewModel
                        ).collectLatest { pagingData ->
                            _uiState.value = pagingData
                        }
                    }
                    NOW_PLAYING -> {
                        useCase.getNowPlayingMovies(
                            header,
                            language,
                            this@MovieViewModel
                        ).collectLatest { pagingData ->
                            _uiState.value = pagingData
                        }
                    }

                    TOP_RATED -> {
                        useCase.getTopRatedMovies(
                            header,
                            language,
                            this@MovieViewModel
                        ).collectLatest { pagingData ->
                            _uiState.value = pagingData
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())

            }
        }
    }
}