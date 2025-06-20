package com.kira.android.cinemadb.features.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kira.android.cinemadb.model.MovieResult

class TopRatedMoviePagingSource(
    private val header: String,
    private val remoteSource: MovieRemoteSource
): PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getTopRatedMovies(header, page = currentPage)
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