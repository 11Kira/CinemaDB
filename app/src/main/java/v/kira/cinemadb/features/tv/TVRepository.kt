package v.kira.cinemadb.features.tv

import v.kira.cinemadb.domain.mapMovieResultToDomain
import v.kira.cinemadb.model.CinemaResult
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val remoteSource: TVRemoteSource
) {
    suspend fun getAiringTodayTVShows(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getAiringTodayTVShows(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getPopularTVShows(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getPopularTVShows(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getTopRatedTVShows(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getTopRatedTVShows(token, language, page).mapMovieResultToDomain()
    }
}