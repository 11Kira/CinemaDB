package com.kira.android.cinemadb.features.tv

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.android.cinemadb.model.TVShowResult

class PopularTVShowPagingSource(
    private val header: String,
    private val remoteSource: TVRemoteSource
): PagingSource<Int, TVShowResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShowResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getPopularTVShows(header, page = currentPage)
            val tvShows = response.body()?.results.orEmpty()

            LoadResult.Page(
                data = tvShows,
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