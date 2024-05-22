package v.kira.cinemadb.features.movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
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

    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
        language: String
    ): MovieResult {
        return repository.getMovieDetails(token, movieId, language)
    }

    suspend fun getMovies(viewModel: MovieViewModel): Flow<PagingData<MovieResult>> {
        return repository.getMovieResults().cachedIn(viewModel.viewModelScope)
    }
}