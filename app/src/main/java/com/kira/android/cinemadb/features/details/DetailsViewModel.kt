package com.kira.android.cinemadb.features.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.kira.android.cinemadb.features.account.AccountUseCase
import com.kira.android.cinemadb.util.SettingsPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val detailsUseCase: DetailsUseCase,
    private val accountUseCase: AccountUseCase
): ViewModel() {

    var header: String
    var accountId = 0L

    private val mutableDetailsState: MutableSharedFlow<DetailsState> = MutableSharedFlow()
    val movieState
        get() = mutableDetailsState.asSharedFlow()

    init {
        runBlocking {
            header =  SettingsPrefs(context).getToken.first().toString()
            accountId = SettingsPrefs(context).getAccountId.first()
        }
    }

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = detailsUseCase.getMovieDetails(header, movieId)
            val watchListResult = async { detailsUseCase.getMovieWatchlistDetails(header, movieId) }
            result.accountStates = watchListResult.await()

            mutableDetailsState.emit(DetailsState.SetMovieDetails(result))
        }
    }

    fun getTVShowDetails(tvSeriesId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = detailsUseCase.getTVShowDetails(header, tvSeriesId)
            val watchListResult = async { detailsUseCase.getTVShowWatchlistDetails(header, tvSeriesId) }
            result.accountStates = watchListResult.await()

            mutableDetailsState.emit(DetailsState.SetTvShowDetails(result))
        }
    }

    fun addToWatchlist(mediaType: Int, mediaId: Long, isWatchlist: Boolean) {
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
    }
}