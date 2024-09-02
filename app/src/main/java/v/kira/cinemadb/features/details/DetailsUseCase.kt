package v.kira.cinemadb.features.details

import v.kira.cinemadb.features.movies.MovieRepository
import v.kira.cinemadb.features.tv.TVRepository
import v.kira.cinemadb.model.AccountStates
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository
) {

    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
    ): MovieResult {
        return movieRepository.getMovieDetails(token, movieId)
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
    ): TVShowResult {
        return tvRepository.getTVShowDetails(token, tvSeriesId)
    }

    suspend fun getMovieWatchlistDetails(
        token: String,
        movieId: Long,
    ): AccountStates {
        return movieRepository.getMovieWatchlistDetails(token, movieId)
    }

    suspend fun getTVShowWatchlistDetails(
        token: String,
        tvSeriesId: Long,
    ): AccountStates {
        return tvRepository.getTVShowWatchlistDetails(token, tvSeriesId)
    }
}