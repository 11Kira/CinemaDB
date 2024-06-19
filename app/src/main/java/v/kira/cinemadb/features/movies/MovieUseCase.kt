package v.kira.cinemadb.features.movies

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getTopRatedMovies(token: String): Flow<PagingData<MovieResult>> {
        return repository.getTopRatedMovies(token)
    }

    fun getTrendingMovies(token: String): Flow<PagingData<MovieResult>> {
        return repository.getTrendingMovies(token)
    }
}