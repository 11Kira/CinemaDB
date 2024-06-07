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
        language: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getAiringTodayTVShows(token, language)
    }

    fun getTopRatedTVShows(
        token: String,
        language: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTopRatedTVShows(token, language)
    }

    fun getTrendingTVShows(
        token: String,
        language: String,
    ): Flow<PagingData<TVShowResult>> {
        return repository.getTrendingTVShows(token, language)
    }
}