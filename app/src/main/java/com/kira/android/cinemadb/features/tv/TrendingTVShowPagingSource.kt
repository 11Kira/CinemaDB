package com.kira.android.cinemadb.features.tv

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.android.cinemadb.model.TVShowResult

class TrendingTVShowPagingSource(
    private val header: String,
    private val remoteSource: TVRemoteSource
): PagingSource<Int, TVShowResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShowResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getTrendingTVShows(header, page = currentPage)
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

    override fun getRefreshKey(state: PagingState<Int, TVShowResult>): Int? {
        return null
    }
}