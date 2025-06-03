package com.kira.android.cinemadb.features.tv

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kira.android.cinemadb.MainActivity.Companion.TOP_RATED
import com.kira.android.cinemadb.MainActivity.Companion.TRENDING
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.util.SettingsPrefs
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
import javax.inject.Inject

@HiltViewModel
class TVViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val useCase: TVUseCase,
): ViewModel() {

    lateinit var header: String

    private val _tvShowPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(PagingData.empty())
    val tvShowPagingState: StateFlow<PagingData<TVShowResult>>
        get() = _tvShowPagingState.asStateFlow()

    private val _selectedTVShowTab = MutableStateFlow("Trending")
    val selectedTVShowTab: StateFlow<String>
        get() = _selectedTVShowTab.asStateFlow()

    fun updateSelectedTVShowTab(selectedTab: String) { _selectedTVShowTab.value = selectedTab }

    private val _scrollToTopState = MutableStateFlow(false)
    val scrollToTopState: StateFlow<Boolean>
        get() = _scrollToTopState.asStateFlow()

    fun updateScrollToTopState(scrollToTop: Boolean) { _scrollToTopState.value = scrollToTop }

    init {
        viewModelScope.launch {
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