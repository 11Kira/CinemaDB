package v.kira.cinemadb.features.tv

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.domain.mapTVShowDetailsToDomain
import v.kira.cinemadb.model.AccountStates
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val remoteSource: TVRemoteSource
) {
    fun getTrendingTVShows(token: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TrendingTVShowPagingSource(token, remoteSource)
        }.flow
    fun getTopRatedTVShows(token: String): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TopRatedTVShowPagingSource(token, remoteSource)
        }.flow

    suspend fun getTVShowDetails(token: String, tvSeriesId: Long): TVShowResult {
        return remoteSource.getTVShowDetails(token, tvSeriesId).mapTVShowDetailsToDomain()
    }

    suspend fun getTVShowWatchlistDetails(token: String, tvSeriesId: Long): AccountStates {
        return remoteSource.getTVShowWatchlistDetails(token, tvSeriesId)
    }
}