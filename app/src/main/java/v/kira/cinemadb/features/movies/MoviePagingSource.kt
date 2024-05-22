package v.kira.cinemadb.features.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import v.kira.cinemadb.model.MovieResult
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val remoteSource: MovieRemoteSource
): PagingSource<Int, MovieResult>() {

    val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmJlYWE5ZjdhYzAwN2YwMDg4ZDBmOWM1NzZjZmU4NCIsInN1YiI6IjVhNTU5ZGUxYzNhMzY4NWVlNjAxYjU0ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.A_W6hgICqOtrDQl_CfvLEZ8XFeOJ7cnKQZ-_ablYfQY"
    val header = "Bearer $token"
    val language = "en-US"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        return try {
            val currentPage = params.key ?: 1
            val response = remoteSource.getTrendingMovies(header, language, page = currentPage)
            val movies = response.body()?.results

            LoadResult.Page(
                data = movies!!,
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