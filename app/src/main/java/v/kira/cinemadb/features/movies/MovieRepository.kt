package v.kira.cinemadb.features.movies

import v.kira.cinemadb.domain.mapMovieResultToDomain
import v.kira.cinemadb.model.CinemaResult
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteSource: MovieRemoteSource
) {
    suspend fun getNowPlayingMovies(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getNowPlayingMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getPopularMovies(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getPopularMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getTopRatedMovies(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getTopRatedMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getUpcomingMovies(token: String, language: String, page: Int): List<CinemaResult> {
        return remoteSource.getUpcomingMovies(token, language, page).mapMovieResultToDomain()
    }
}