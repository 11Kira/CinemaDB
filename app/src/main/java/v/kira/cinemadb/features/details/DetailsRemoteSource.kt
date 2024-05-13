package v.kira.cinemadb.features.details

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRemoteSource @Inject constructor(
    private val detailsService: DetailsService
) {

    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
        language: String
    ) = withContext(Dispatchers.IO) {
        detailsService.getMovieDetails(
            header = token,
            movieId = movieId,
            language = language
        )
    }
}