package com.kira.android.cinemadb.features.search

import androidx.paging.PagingData
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    fun searchMovie(token: String, query: String): Flow<PagingData<MovieResult>> {
        return repository.searchMovie(token, query)
    }
    fun searchTVShow(token: String, query: String): Flow<PagingData<TVShowResult>> {
        return repository.searchTVShow(token, query)
    }
}