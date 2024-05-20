package v.kira.cinemadb.features.movies

import v.kira.cinemadb.domain.mapMovieDetailsToDomain
import v.kira.cinemadb.domain.mapMovieResultToDomain
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteSource: MovieRemoteSource
) {
    suspend fun getNowPlayingMovies(token: String, language: String, page: Int): List<MovieResult> {
        return remoteSource.getNowPlayingMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getTrendingMovies(token: String, language: String, page: Int): List<MovieResult> {
        return remoteSource.getTrendingMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getTopRatedMovies(token: String, language: String, page: Int): List<MovieResult> {
        return remoteSource.getTopRatedMovies(token, language, page).mapMovieResultToDomain()
    }
    suspend fun getMovieDetails(token: String, movieId: Long, language: String): MovieResult {
        return remoteSource.getMovieDetails(token, movieId, language).mapMovieDetailsToDomain()
    }
}