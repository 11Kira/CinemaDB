package v.kira.cinemadb.features.tv

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class TVUseCase @Inject constructor(
    private val repository: TVRepository
) {

    fun getAiringTodayTVShows(
        token: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getAiringTodayTVShows(token)
    }

    fun getTopRatedTVShows(
        token: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTopRatedTVShows(token)
    }

    fun getTrendingTVShows(
        token: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTrendingTVShows(token)
    }
}