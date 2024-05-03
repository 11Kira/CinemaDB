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
    suspend fun getPopularTVShows(token: String, language: String, page: Int): List<TVResult> {
        return remoteSource.getPopularTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
    suspend fun getTopRatedTVShows(token: String, language: String, page: Int): List<TVResult> {
        return remoteSource.getTopRatedTVShows(token, language, page).mapTVSeriesResultToDomain()
    }
}