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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    lateinit var header: String

    private val _moviesPagingState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(PagingData.empty())
    val moviesPagingState: StateFlow<PagingData<MovieResult>>
        get() = _moviesPagingState.asStateFlow()

    private val _selectedMovieTab = MutableStateFlow("Trending")
    val selectedMovieTab: StateFlow<String>
        get() = _selectedMovieTab.asStateFlow()

    fun updateSelectedMovieTab(selectedTab: String) { _selectedMovieTab.value = selectedTab }

    private var _scrollToTopState = MutableStateFlow(false)
    val scrollToTopState: StateFlow<Boolean>
        get() = _scrollToTopState.asStateFlow()

    fun updateScrollToTopState(scrollToTop: Boolean) {
        _scrollToTopState.value = scrollToTop
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            async { header =  SettingsPrefs(context).getToken.first().toString() }.await()
            getMovies(TRENDING)
        }
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
                            .getTrendingMovies(header)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _moviesPagingState.value = pagingData
                            }
                    }

                    TOP_RATED -> {
                        useCase
                            .getTopRatedMovies(header)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _moviesPagingState.value = pagingData
                            }
                    }
                }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }
}