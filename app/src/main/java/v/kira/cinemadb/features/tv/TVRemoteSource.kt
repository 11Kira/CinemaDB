package v.kira.cinemadb.features.tv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TVRemoteSource @Inject constructor(
    private val tvService: TVService
) {

    suspend fun getAiringTodayTVShows(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getAiringTodayTvShows(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getPopularTVShows(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getPopularTvShows(
            header = token,
            language = language,
            page = page,
        )
    }

    suspend fun getTopRatedTVShows(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getTopRatedTvShows(
            header = token,
            language = language,
            page = page
        )
    }
}