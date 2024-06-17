package v.kira.cinemadb.features.tv

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
import v.kira.cinemadb.model.TVShowResult
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class TVViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val useCase: TVUseCase,
): ViewModel() {

    var header: String

    private val _tvShowPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(
        PagingData.empty())
    val tvShowPagingState: StateFlow<PagingData<TVShowResult>> = _tvShowPagingState.asStateFlow()

    private val _scrollToTopState = MutableStateFlow(false)
    val scrollToTopState: StateFlow<Boolean> = _scrollToTopState.asStateFlow()

    fun updateScrollToTopState(scrollToTop: Boolean) {
        _scrollToTopState.value = scrollToTop
    }

    init {
        runBlocking {
            header =  SettingsPrefs(context).getToken.first().toString()
            getTVShowList(TRENDING)
        }
    }

    fun getTVShowList(type: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                when (type) {
                    TRENDING -> {
                        useCase
                            .getTrendingTVShows(header)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _tvShowPagingState.value = pagingData
                            }
                    }
                    NOW_PLAYING -> {
                        useCase
                            .getAiringTodayTVShows(header)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _tvShowPagingState.value = pagingData
                            }
                    }

                    TOP_RATED -> {
                        useCase
                            .getTopRatedTVShows(header)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                _tvShowPagingState.value = pagingData
                            }
                    }
                }

            } catch (e: Exception) {
                Log.d("Exception:", e.toString())

            }
        }
    }
}