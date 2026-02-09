package com.kira.android.cinemadb.features.reviews

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.android.cinemadb.features.movies.MovieUseCase
import com.kira.android.cinemadb.features.tv.TVUseCase
import com.kira.android.cinemadb.util.SettingsPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserReviewViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val movieUseCase: MovieUseCase,
    private val tvUseCase: TVUseCase
) : ViewModel() {

    var header: String
    var accountId = 0L

    init {
        runBlocking {
            header = SettingsPrefs(context).getToken.first().toString()
            accountId = SettingsPrefs(context).getAccountId.first()
        }
    }

    fun getMovieReviews(id: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            movieUseCase.getMovieReviews(header, id)
        }
    }

    fun getTVShowReviews(id: Long) {
        viewModelScope.launch(CoroutineExceptionHandler { _, error ->
            runBlocking {
                Log.e("ERROR", error.message.toString())
            }
        }) {
            tvUseCase.getTVShowReviews(header, id)
        }
    }
}