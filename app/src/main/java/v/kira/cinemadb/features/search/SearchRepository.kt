package v.kira.cinemadb.features.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val remoteSource: SearchRemoteSource
) {
    fun searchMovie(token: String, query: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            SearchMoviePagingSource(token, query, remoteSource)
        }.flow
    fun searchTVShow(token: String, query: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            SearchTVShowPagingSource(token, query, remoteSource)
        }.flow
}