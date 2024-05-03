package v.kira.cinemadb.features.tv

import v.kira.cinemadb.domain.mapTVSeriesResultToDomain
import v.kira.cinemadb.model.TVResult
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val remoteSource: TVRemoteSource
) {
    suspend fun getAiringTodayTVShows(token: String, language: String, page: Int): List<TVResult> {
        return remoteSource.getAiringTodayTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTrendingTVShows(token: String, language: String, page: Int): List<TVResult> {
        return remoteSource.getTrendingTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTopRatedTVShows(token: String, language: String, page: Int): List<TVResult> {
        return remoteSource.getTopRatedTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
}