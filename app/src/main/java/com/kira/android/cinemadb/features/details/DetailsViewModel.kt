package com.kira.android.cinemadb.features.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.android.cinemadb.domain.AccountUseCase
import com.kira.android.cinemadb.domain.DetailsUseCase
import com.kira.android.cinemadb.utils.SettingsPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val detailsUseCase: DetailsUseCase,
    private val accountUseCase: AccountUseCase,
): ViewModel() {

    var header: String
    var accountId = 0L

    private val _detailsUiState = MutableStateFlow(DetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _detailsEffect = Channel<DetailsUiEffect>(Channel.BUFFERED)
    val detailsEffect = _detailsEffect.receiveAsFlow()

    init {
        runBlocking {
            header = SettingsPrefs(context).getToken.first()
            accountId = SettingsPrefs(context).getAccountId.first()
        }
    }

    fun getMovieDetails(movieId: Long) {
        if (_detailsUiState.value.movieDetails?.id == movieId) return
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val response = detailsUseCase.getMovieDetails(header, movieId)
                _detailsUiState.update { it.copy(movieDetails = response) }
                /*if (response != null) {

                } else {
                    _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = "Something went wrong"))
                }*/
            } catch (e: Exception) {
                /* val errorMessage = networkUtils.parseNetworkError(e)
                 _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))*/
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }

    }

    fun getTVShowDetails(tvSeriesId: Long) {
        if (_detailsUiState.value.tvShowDetails?.id == tvSeriesId) return
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val response = detailsUseCase.getTVShowDetails(header, tvSeriesId)
                _detailsUiState.update { it.copy(tvShowDetails = response) }
                /*if (response != null) {

                } else {
                    _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = "Something went wrong"))
                }*/
            } catch (e: Exception) {
                /* val errorMessage = networkUtils.parseNetworkError(e)
                 _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))*/
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /*fun addToWatchlist(mediaType: Int, mediaId: Long, isWatchlist: Boolean) {
        val jsonObject = JsonObject()
        val type = if (mediaType == 1) "movie" else "tv"
        jsonObject.addProperty("media_type", type)
        jsonObject.addProperty("media_id", mediaId)
        jsonObject.addProperty("watchlist", isWatchlist)
        submitWatchlistObject(jsonObject, mediaType, mediaId)
    }

    private fun submitWatchlistObject(jsonObject: JsonObject, mediaType: Int, mediaId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val invoke = accountUseCase.addToWatchlist(header, accountId, jsonObject)
            if (mediaType == 1 && invoke.success) {
                getMovieDetails(mediaId)
            } else {
                getTVShowDetails(mediaId)
            }
        }
    }*/
}