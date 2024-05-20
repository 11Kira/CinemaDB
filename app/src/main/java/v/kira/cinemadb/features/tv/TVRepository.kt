package v.kira.cinemadb.features.tv

import v.kira.cinemadb.domain.mapTVSeriesResultToDomain
import v.kira.cinemadb.domain.mapTVShowDetailsToDomain
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val remoteSource: TVRemoteSource
) {
    suspend fun getAiringTodayTVShows(token: String, language: String, page: Int): List<TVShowResult> {
        return remoteSource.getAiringTodayTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTrendingTVShows(token: String, language: String, page: Int): List<TVShowResult> {
        return remoteSource.getTrendingTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTopRatedTVShows(token: String, language: String, page: Int): List<TVShowResult> {
        return remoteSource.getTopRatedTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTVShowDetails(token: String, tvSeriesId: Long, language: String): TVShowResult {
        return remoteSource.getTVShowDetails(token, tvSeriesId, language).mapTVShowDetailsToDomain()
    }
}