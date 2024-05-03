package v.kira.cinemadb.features.movies

import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend fun getNowPlayingMovies(
        token: String,
        language: String,
        page: Int
    ): List<MovieResult> {
        return repository.getNowPlayingMovies(token, language, page)
    }

    suspend fun getTopRatedMovies(
        token: String,
        language: String,
        page: Int
    ): List<MovieResult> {
        return repository.getTopRatedMovies(token, language, page)
    }

    suspend fun getTrendingMovies(
        token: String,
        language: String,
        page: Int
    ): List<MovieResult> {
        return repository.getTrendingMovies(token, language, page)
    }

    suspend fun getUpcomingMovies(
        token: String,
        language: String,
        page: Int
    ): List<MovieResult> {
        return repository.getUpcomingMovies(token, language, page)
    }
}