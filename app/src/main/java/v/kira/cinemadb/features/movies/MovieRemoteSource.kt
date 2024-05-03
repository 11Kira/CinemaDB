package v.kira.cinemadb.features.movies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteSource @Inject constructor(
    private val movieService: MovieService
) {

    suspend fun getNowPlayingMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getNowPlayingMovies(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getTrendingMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getTrendingMovies(
            header = token,
            language = language,
            page = page,
        )
    }

    suspend fun getTopRatedMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getTopRatedMovies(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getUpcomingMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getUpcomingMovies(
            header = token,
            language = language,
            page = page
        )
    }
}