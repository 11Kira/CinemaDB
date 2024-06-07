package v.kira.cinemadb.features.movies

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getNowPlayingMovies(token: String, language: String): Flow<PagingData<MovieResult>> {
        return repository.getNowPlayingMovies(token, language)
    }

    fun getTopRatedMovies(token: String, language: String): Flow<PagingData<MovieResult>> {
        return repository.getTopRatedMovies(token, language)
    }

    fun getTrendingMovies(token: String, language: String): Flow<PagingData<MovieResult>> {
        return repository.getTrendingMovies(token, language)
    }
}