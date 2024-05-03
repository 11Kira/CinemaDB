package v.kira.cinemadb.features.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import v.kira.cinemadb.MainActivity
import v.kira.cinemadb.model.TVResult
import javax.inject.Inject

@HiltViewModel
class TVViewModel @Inject constructor(
    private val useCase: TVUseCase,
): ViewModel() {
    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    private val mutableTVShowState: MutableSharedFlow<TVShowState> = MutableSharedFlow()
    val tvShowState = mutableTVShowState.asSharedFlow()

    fun getTVShowList(type: Int, page: Int) {
        viewModelScope.launch (CoroutineExceptionHandler { _, error ->
            runBlocking {
                mutableTVShowState.emit(TVShowState.ShowError(error))
            }
        }) {
            val tvShowList: List<TVResult>
            when (type) {
                MainActivity.TRENDING -> {
                    tvShowList = useCase.getTrendingTVShows(header, language, page)
                    mutableTVShowState.emit(TVShowState.SetTrendingTVShows(tvShowList))
                }
                MainActivity.NOW_PLAYING -> {
                    tvShowList = useCase.getAiringTodayTVShows(header, language, page)
                    mutableTVShowState.emit(TVShowState.SetAiringTodayTVShows(tvShowList))
                }

                MainActivity.TOP_RATED -> {
                    tvShowList = useCase.getTopRatedTVShows(header, language, page)
                    mutableTVShowState.emit(TVShowState.SetTopRatedTVShows(tvShowList))
                }
            }
        }
    }
}