package v.kira.cinemadb.features.details

import v.kira.cinemadb.domain.mapMovieDetailsToDomain
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class DetailsRepository @Inject constructor(
    private val remoteSource: DetailsRemoteSource
) {
    suspend fun getMovieDetails(token: String, movieId: Long, language: String): MovieResult {
        return remoteSource.getMovieDetails(token, movieId, language).mapMovieDetailsToDomain()
    }
}