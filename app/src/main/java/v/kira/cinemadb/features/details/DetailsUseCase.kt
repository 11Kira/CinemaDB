package v.kira.cinemadb.features.details

import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
        language: String
    ): MovieResult {
        return repository.getMovieDetails(token, movieId, language)
    }
}