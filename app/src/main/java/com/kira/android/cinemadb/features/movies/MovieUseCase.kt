package com.kira.android.cinemadb.features.movies

import androidx.paging.PagingData
import com.kira.android.cinemadb.model.MovieResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getTopRatedMovies(token: String): Flow<PagingData<MovieResult>> {
        return repository.getTopRatedMovies(token)
    }

    fun getPopularMovies(token: String): Flow<PagingData<MovieResult>> {
        return repository.getPopularMovies(token)
    }
}