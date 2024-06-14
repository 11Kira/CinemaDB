package v.kira.cinemadb.features.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult
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