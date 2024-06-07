package v.kira.cinemadb.features.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import v.kira.cinemadb.features.account.AccountUseCase
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val detailsUseCase: DetailsUseCase,
    private val accountUseCase: AccountUseCase
): ViewModel() {

    val language = "en-US"
    var header: String

    private val mutableDetailsState: MutableSharedFlow<DetailsState> = MutableSharedFlow()
    val movieState = mutableDetailsState.asSharedFlow()

    init {
        runBlocking { header =  SettingsPrefs(context).getToken.first().toString() }
    }

    fun getMovieDetails(movieId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = detailsUseCase.getMovieDetails(header, movieId, language)
            mutableDetailsState.emit(DetailsState.SetMovieDetails(result))
        }
    }

    fun getTVShowDetails(tvSeriesId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = detailsUseCase.getTVShowDetails(header, tvSeriesId, language)
            mutableDetailsState.emit(DetailsState.SetTvShowDetails(result))
        }
    }

    fun addToWatchlist(mediaType: Int, mediaId: Long, isWatchlist: Boolean) {
        val jsonObject = JsonObject()
        val type = if (mediaType == 1) "movie" else "tv"
        jsonObject.addProperty("media_type", type)
        jsonObject.addProperty("media_id", mediaId)
        jsonObject.addProperty("watchlist", isWatchlist)
        submitWatchlistObject(jsonObject)
    }

    private fun submitWatchlistObject(jsonObject: JsonObject) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            accountUseCase.addToWatchlist(header, 7749280, jsonObject)
        }
    }
}