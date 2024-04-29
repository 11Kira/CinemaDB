package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.CinemaResult
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend fun getNowPlayingMovies(
        token: String,
        language: String,
        page: Int
    ): List<CinemaResult> {
        return repository.getNowPlayingMovies(token, language, page)
    }

    suspend fun getTopRatedMovies(
        token: String,
        language: String,
        page: Int
    ): List<CinemaResult> {
        return repository.getTopRatedMovies(token, language, page)
    }

    suspend fun getPopularMovies(
        token: String,
        language: String,
        page: Int
    ): List<CinemaResult> {
        return repository.getPopularMovies(token, language, page)
    }
}