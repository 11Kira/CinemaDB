package v.kira.cinemadb.features.tv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import v.kira.cinemadb.MainActivity.Companion.NOW_PLAYING
import v.kira.cinemadb.MainActivity.Companion.TOP_RATED
import v.kira.cinemadb.MainActivity.Companion.TRENDING
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

@HiltViewModel
class TVViewModel @Inject constructor(
    private val useCase: TVUseCase,
): ViewModel() {
    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    private val tvShowPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(
        PagingData.empty())
    val uiState: StateFlow<PagingData<TVShowResult>> = tvShowPagingState.asStateFlow()

    fun getTVShowList(type: Int) {
        viewModelScope.launch {
            try {
                when (type) {
                    TRENDING -> {
                        useCase
                            .getTrendingTVShows(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                tvShowPagingState.value = pagingData
                            }
                    }
                    NOW_PLAYING -> {
                        useCase
                            .getAiringTodayTVShows(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                tvShowPagingState.value = pagingData
                            }
                    }

                    TOP_RATED -> {
                        useCase
                            .getTopRatedTVShows(header, language)
                            .cachedIn(viewModelScope)
                            .collectLatest { pagingData ->
                                tvShowPagingState.value = pagingData
                            }
                    }
                }

            } catch (e: Exception) {
                Log.d("Exception:", e.toString())

            }
        }
    }
}