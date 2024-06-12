package v.kira.cinemadb.features.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.domain.mapMovieDetailsToDomain
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteSource: MovieRemoteSource
) {
    fun getNowPlayingMovies(token: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            NowPlayingMoviePagingSource(token, remoteSource)
        }.flow

    fun getTopRatedMovies(token: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TopRatedMoviePagingSource(token, remoteSource)
        }.flow

    fun getTrendingMovies(token: String): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TrendingMoviePagingSource(token, remoteSource)
        }.flow

    suspend fun getMovieDetails(token: String, movieId: Long): MovieResult {
        return remoteSource.getMovieDetails(token, movieId).mapMovieDetailsToDomain()
    }
}