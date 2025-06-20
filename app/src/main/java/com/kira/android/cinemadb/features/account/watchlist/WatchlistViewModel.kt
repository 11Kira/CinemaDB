package com.kira.android.cinemadb.features.account.watchlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kira.android.cinemadb.features.account.AccountUseCase
import com.kira.android.cinemadb.model.MovieResult
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
class WatchlistViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val useCase: AccountUseCase
): ViewModel() {

    private val movieWatchlistPagingState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(
        PagingData.empty())
    val movieWatchlistState: StateFlow<PagingData<MovieResult>>
        get() = movieWatchlistPagingState.asStateFlow()

    private val tvShowWatchlistPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(
        PagingData.empty())
    val tvShowWatchlistState: StateFlow<PagingData<TVShowResult>>
        get() = tvShowWatchlistPagingState.asStateFlow()

    private val _selectedWatchlistTab = MutableStateFlow("Movies")
    val selectedWatchlistTab: StateFlow<String>
        get() = _selectedWatchlistTab.asStateFlow()

    fun updateSelectedWatchlistTab(selectedTab: String) { _selectedWatchlistTab.value = selectedTab }

    private var _scrollToTopState = MutableStateFlow(false)
    val scrollToTopState: StateFlow<Boolean>
        get() = _scrollToTopState.asStateFlow()

    fun updateScrollToTopState(scrollToTop: Boolean) {
        _scrollToTopState.value = scrollToTop
    }

    private var _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean>
        get() = _loadingState.asStateFlow()

    fun updateLoadingState(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    lateinit var header: String
    var accountId: Long = 0

    init {
        viewModelScope.launch {
            header = SettingsPrefs(context).getToken.first().toString()
            accountId = SettingsPrefs(context).getAccountId.first()
            getMovieWatchlist()
        }
    }

    fun getMovieWatchlist() {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                useCase
                    .getMovieWatchlist(header, accountId)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        movieWatchlistPagingState.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }

    fun getTVShowWatchlist() {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                useCase
                    .getTVShowWatchlist(header, accountId)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        tvShowWatchlistPagingState.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }
}