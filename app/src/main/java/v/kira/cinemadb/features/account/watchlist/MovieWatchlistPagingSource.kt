package v.kira.cinemadb.features.account.watchlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import v.kira.cinemadb.features.account.AccountRemoteSource
import v.kira.cinemadb.model.MovieResult

class MovieWatchlistPagingSource(
    private val header: String,
    private val accountId: Long,
    private val remoteSource: AccountRemoteSource
): PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getMovieWatchlist(header, accountId, currentPage)
            val movies = response.body()?.results.orEmpty()

            LoadResult.Page(
                data = movies,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (currentPage < response.body()?.totalPages!!) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        return null
    }
}