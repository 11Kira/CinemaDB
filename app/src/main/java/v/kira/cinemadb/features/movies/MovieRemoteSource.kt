package v.kira.cinemadb.features.movies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteSource @Inject constructor(
    private val movieAPI: MovieAPI
) {

    suspend fun getNowPlayingMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieAPI.getNowPlayingMovies(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getPopularMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieAPI.getPopularMovies(
            header = token,
            language = language,
            page = page
        )
    }

    suspend fun getTopRatedMovies(
        token: String,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieAPI.getTopRatedMovies(
            header = token,
            language = language,
            page = page
        )
    }
}