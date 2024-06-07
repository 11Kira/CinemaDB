package v.kira.cinemadb.features.details

import android.content.Context
import android.util.Log
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
import v.kira.cinemadb.features.movies.MovieUseCase
import v.kira.cinemadb.features.tv.TVUseCase
import v.kira.cinemadb.util.SettingsPrefs
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    private val movieUseCase: MovieUseCase,
    private val tvShowUseCase: TVUseCase
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
            val result = movieUseCase.getMovieDetails(header, movieId, language)
            mutableDetailsState.emit(DetailsState.SetMovieDetails(result))
        }
    }

    fun getTVShowDetails(tvSeriesId: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
            val result = tvShowUseCase.getTVShowDetails(header, tvSeriesId, language)
            mutableDetailsState.emit(DetailsState.SetTvShowDetails(result))
        }
    }

    fun addToWatchlist(mediaType: Int, mediaId: Long, isWatchlist: Boolean) {
        val jsonObject = JsonObject()
        val data = JsonObject()
        val type = if (mediaType == 1) "movie" else "tv"
        data.addProperty("media_type", type)
        data.addProperty("media_id", mediaId)
        data.addProperty("watchlist", isWatchlist)
        jsonObject.add("data", data)

        Log.e("testJSON", jsonObject.toString())
    }

    fun submitWatchlistObject(jsonObject: JsonObject) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableDetailsState.emit(DetailsState.ShowError(error))
            }
        }) {
        }
    }
}