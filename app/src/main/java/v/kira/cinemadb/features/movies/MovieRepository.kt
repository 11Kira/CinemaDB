package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.CinemaResult
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteSource: MovieRemoteSource
) {
    suspend fun getTopRatedMovies(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getTopRatedMovies(token, language, page).mapMovieResponseToDomain()
    }
}