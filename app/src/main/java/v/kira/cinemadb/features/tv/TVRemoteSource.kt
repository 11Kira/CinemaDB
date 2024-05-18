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
        tvService.getAiringTodayTVShows(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getTrendingTVShows(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        tvService.getTrendingTVShows(
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
        tvService.getTopRatedTVShows(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
        language: String
    ) = withContext(Dispatchers.IO) {
        tvService.getTVShowDetails(
            header = token,
            seriesId = tvSeriesId,
            language = language
        )
    }
}