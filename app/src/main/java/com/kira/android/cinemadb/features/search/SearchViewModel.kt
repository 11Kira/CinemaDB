package com.kira.android.cinemadb.features.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
class SearchViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val searchUseCase: SearchUseCase
): ViewModel() {
    lateinit var header: String

    private val movieSearchPagingState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(
        PagingData.empty())
    val movieSearchState: StateFlow<PagingData<MovieResult>>
        get() = movieSearchPagingState.asStateFlow()

    private val _selectedSearchTab = MutableStateFlow("Movies")
    val selectedSearchTab: StateFlow<String>
        get() = _selectedSearchTab.asStateFlow()

    fun updateSelectedSearchTab(selectedTab: String) { _selectedSearchTab.value = selectedTab }

    private val tvShowSearchPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(
        PagingData.empty())
    val tvShowSearchState: StateFlow<PagingData<TVShowResult>>
        get() = tvShowSearchPagingState.asStateFlow()

    private val mutableSearchText = MutableStateFlow("")
    val searchText
        get() = mutableSearchText.asStateFlow()

    private val _scrollToTopState = MutableStateFlow(false)
    val scrollToTopState: StateFlow<Boolean>
        get() = _scrollToTopState.asStateFlow()

    fun updateScrollToTopState(scrollToTop: Boolean) { _scrollToTopState.value = scrollToTop }

    init {
        runBlocking {
            header =  SettingsPrefs(context).getToken.first().toString()
        }
    }

    fun onSearchMovieTextChange(text: String) {
        mutableSearchText.value = text
        searchMovie(text)
    }

    fun clearSearch() {
        searchMovie("")
        searchTVShow("")
    }

    private fun searchMovie(query: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                searchUseCase
                    .searchMovie(header, query)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        movieSearchPagingState.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }

    fun onSearchTVShowTextChange(text: String) {
        mutableSearchText.value = text
        searchTVShow(text)
    }

    private fun searchTVShow(query: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            try {
                searchUseCase
                    .searchTVShow(header, query)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        tvShowSearchPagingState.value = pagingData
                    }
            } catch (e: Exception) {
                Log.d("Exception:", e.toString())
            }
        }
    }
}