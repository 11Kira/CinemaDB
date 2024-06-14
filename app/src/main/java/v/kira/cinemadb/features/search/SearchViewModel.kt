package v.kira.cinemadb.features.search

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
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val searchUseCase: SearchUseCase
): ViewModel() {
    var header: String

    private val movieSearchPagingState: MutableStateFlow<PagingData<MovieResult>> = MutableStateFlow(
        PagingData.empty())
    val movieSearchState: StateFlow<PagingData<MovieResult>> = movieSearchPagingState.asStateFlow()

    private val tvShowSearchPagingState: MutableStateFlow<PagingData<TVShowResult>> = MutableStateFlow(
        PagingData.empty())
    val tvShowSearchState: StateFlow<PagingData<TVShowResult>> = tvShowSearchPagingState.asStateFlow()

    private val mutableSearchText = MutableStateFlow("")
    val searchText = mutableSearchText.asStateFlow()


    init {
        runBlocking { header =  SettingsPrefs(context).getToken.first().toString() }
    }

    fun onSearchMovieTextChange(text: String) {
        mutableSearchText.value = text
        searchMovie(text)
    }

    fun searchMovie(query: String) {
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

    fun searchTVShow(query: String) {
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