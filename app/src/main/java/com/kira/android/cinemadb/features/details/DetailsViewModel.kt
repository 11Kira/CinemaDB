package com.kira.android.cinemadb.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.kira.android.cinemadb.domain.AccountUseCase
import com.kira.android.cinemadb.domain.DetailsUseCase
import com.kira.android.cinemadb.model.enums.MediaType
import com.kira.android.cinemadb.utils.NetworkUtils
import com.kira.android.cinemadb.utils.SettingsPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsUseCase: DetailsUseCase,
    private val accountUseCase: AccountUseCase,
    private val settingsPrefs: SettingsPrefs,
    private val networkUtils: NetworkUtils,
) : ViewModel() {

    private val _detailsUiState = MutableStateFlow(DetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _detailsEffect = Channel<DetailsUiEffect>(Channel.BUFFERED)
    val detailsEffect = _detailsEffect.receiveAsFlow()

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val header = settingsPrefs.getToken.first()
                val response = detailsUseCase.getMovieDetails(header, movieId)
                _detailsUiState.update { it.copy(movieDetails = response) }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getWatchlistStatus(mediaId: Long, mediaType: String) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val header = settingsPrefs.getToken.first()
                if (mediaType == MediaType.MOVIE.name) {
                    val response = detailsUseCase.getMovieWatchlistDetails(header, mediaId)
                    _detailsUiState.update { it.copy(movieWatchlistStatus = response.watchlist) }
                } else {
                    val response = detailsUseCase.getTVShowWatchlistDetails(header, mediaId)
                    _detailsUiState.update { it.copy(tvShowWatchlistStatus = response.watchlist) }
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
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
                val header = settingsPrefs.getToken.first()
                val response = detailsUseCase.getTVShowDetails(header, tvSeriesId)
                _detailsUiState.update { it.copy(tvShowDetails = response) }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun submitWatchlistObject(jsonObject: JsonObject, mediaType: String, mediaId: Long) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val header = settingsPrefs.getToken.first()
                val accountId = settingsPrefs.getAccountId.first()
                val response = accountUseCase.addToWatchlist(header, accountId, jsonObject)
                if (mediaType == MediaType.MOVIE.name && response.success) {
                    getWatchlistStatus(mediaId, MediaType.MOVIE.name)
                } else {
                    getWatchlistStatus(mediaId, MediaType.TV_SHOW.name)
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun addToWatchlist(mediaType: String, mediaId: Long, isWatchlist: Boolean) {
        val jsonObject = JsonObject()
        val type = if (mediaType == MediaType.MOVIE.name) "movie" else "tv"
        jsonObject.addProperty("media_type", type)
        jsonObject.addProperty("media_id", mediaId)
        jsonObject.addProperty("watchlist", isWatchlist)
        submitWatchlistObject(jsonObject, mediaType, mediaId)
    }
}