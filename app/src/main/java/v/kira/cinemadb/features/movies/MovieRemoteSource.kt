package v.kira.cinemadb.features.movies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRemoteSource @Inject constructor(
    private val movieService: MovieService
) {
    suspend fun getTrendingMovies(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getTrendingMovies(
            header = token,
            page = page,
        )
    }

    suspend fun getTopRatedMovies(
        token: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        movieService.getTopRatedMovies(
            header = token,
            page = page
        )
    }

    suspend fun getMovieDetails(
        token: String,
        movieId: Long
    ) = withContext(Dispatchers.IO) {
        movieService.getMovieDetails(
            header = token,
            movieId = movieId
        )
    }
}