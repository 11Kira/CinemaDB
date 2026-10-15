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
import kotlinx.coroutines.async
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

    fun getMediaDetails(mediaId: Long, mediaType: String) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val header = settingsPrefs.getToken.first()
                if (mediaType == MediaType.MOVIE.name) {
                    val detailsDeferred = async { detailsUseCase.getMovieDetails(header, mediaId) }
                    val watchlistDeferred =
                        async { detailsUseCase.getMovieWatchlistDetails(header, mediaId) }

                    val details = detailsDeferred.await()
                    val watchlist = watchlistDeferred.await()

                    _detailsUiState.update {
                        it.copy(
                            movieDetails = details,
                            movieWatchlistStatus = watchlist.watchlist
                        )
                    }
                } else {
                    val detailsDeferred = async { detailsUseCase.getTVShowDetails(header, mediaId) }
                    val watchlistDeferred =
                        async { detailsUseCase.getMovieWatchlistDetails(header, mediaId) }

                    val details = detailsDeferred.await()
                    val watchlist = watchlistDeferred.await()

                    _detailsUiState.update {
                        it.copy(
                            tvShowDetails = details,
                            tvShowWatchlistStatus = watchlist.watchlist
                        )
                    }
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun toggleWatchlist(mediaType: String, mediaId: Long, targetWatchlistState: Boolean) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true) }
            try {
                val header = settingsPrefs.getToken.first()
                val accountId = settingsPrefs.getAccountId.first()
                val jsonObject = JsonObject().apply {
                    addProperty(
                        "media_type",
                        if (mediaType == MediaType.MOVIE.name) "movie" else "tv"
                    )
                    addProperty("media_id", mediaId)
                    addProperty("watchlist", targetWatchlistState)
                }
                val response = accountUseCase.addToWatchlist(header, accountId, jsonObject)
                if (response.success) {
                    _detailsUiState.update { state ->
                        if (mediaType == MediaType.MOVIE.name) {
                            state.copy(movieWatchlistStatus = targetWatchlistState)
                        } else {
                            state.copy(tvShowWatchlistStatus = targetWatchlistState)
                        }
                    }
                }
            } catch (e: Exception) {
                val errorMessage = networkUtils.parseNetworkError(e)
                _detailsEffect.send(DetailsUiEffect.ShowSnackbar(message = errorMessage))
            } finally {
                _detailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }
}