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
    suspend fun getNowPlayingMovies(token: String, language: String, viewModel: MovieViewModel): Flow<PagingData<MovieResult>> {
        return repository.getNowPlayingMovies(token, language).cachedIn(viewModel.viewModelScope)
    }

    suspend fun getTopRatedMovies(token: String, language: String, viewModel: MovieViewModel): Flow<PagingData<MovieResult>> {
        return repository.getTopRatedMovies(token, language).cachedIn(viewModel.viewModelScope)
    }

    suspend fun getTrendingMovies(token: String, language: String, viewModel: MovieViewModel): Flow<PagingData<MovieResult>> {
        return repository.getTrendingMovies(token, language).cachedIn(viewModel.viewModelScope)
    }

    suspend fun getMovieDetails(
        token: String,
        movieId: Long,
        language: String
    ): MovieResult {
        return repository.getMovieDetails(token, movieId, language)
    }
}