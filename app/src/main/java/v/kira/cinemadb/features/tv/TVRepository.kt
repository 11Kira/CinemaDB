package v.kira.cinemadb.features.tv

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.domain.mapTVShowDetailsToDomain
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val remoteSource: TVRemoteSource
) {
    fun getAiringTodayTVShows(token: String, language: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            AiringTodayTVShowPagingSource(token, language, remoteSource)
        }.flow
    fun getTrendingTVShows(token: String, language: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TrendingTVShowPagingSource(token, language, remoteSource)
        }.flow
    fun getTopRatedTVShows(token: String, language: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TopRatedTVShowPagingSource(token, language, remoteSource)
        }.flow

    suspend fun getTVShowDetails(token: String, tvSeriesId: Long, language: String): TVShowResult {
        return remoteSource.getTVShowDetails(token, tvSeriesId, language).mapTVShowDetailsToDomain()
    }
}