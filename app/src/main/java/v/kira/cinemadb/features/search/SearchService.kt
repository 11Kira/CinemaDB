package v.kira.cinemadb.features.search

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import v.kira.cinemadb.model.ListResponseObject
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult

interface SearchService {
    @GET("search/movie")
    suspend fun searchMovie(
        @Header("Authorization") header: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<MovieResult>>>

    @GET("search/tv")
    suspend fun searchTVShow(
        @Header("Authorization") header: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<TVShowResult>>>
}