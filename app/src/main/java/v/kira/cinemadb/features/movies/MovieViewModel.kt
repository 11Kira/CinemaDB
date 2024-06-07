package v.kira.cinemadb.features.movies

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import v.kira.cinemadb.MainActivity.Companion.NOW_PLAYING
import v.kira.cinemadb.MainActivity.Companion.TOP_RATED
import v.kira.cinemadb.MainActivity.Companion.TRENDING
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val useCase: MovieUseCase,
): ViewModel() {

    val language = "en-US"
    var header: String

    private val moviesPagingState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(PagingData.empty())
    val uiState: StateFlow<PagingData<MovieResult>> = moviesPagingState.asStateFlow()

    init {
        runBlocking { header =  SettingsPrefs(context).getToken.first().toString() }
    }

    fun getMovies(type: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                when (type) {
                    TRENDING -> {
                        useCase
                            .getTrendingMovies(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                            moviesPagingState.value = pagingData
                            }
                    }
                    NOW_PLAYING -> {
                        useCase
                            .getNowPlayingMovies(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                moviesPagingState.value = pagingData
                            }
                    }

                    TOP_RATED -> {
                        useCase
                            .getTopRatedMovies(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                moviesPagingState.value = pagingData
                            }
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }
}