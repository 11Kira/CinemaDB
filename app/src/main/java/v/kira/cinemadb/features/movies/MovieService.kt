package v.kira.cinemadb.features.movies

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import v.kira.cinemadb.model.ListResponseObject
import v.kira.cinemadb.model.MovieResult

interface MovieService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Header("Authorization") header: String,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<MovieResult>>>

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Header("Authorization") header: String,
        @Query("page") page: Int,
    ): Response<ListResponseObject<List<MovieResult>>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") header: String,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<MovieResult>>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") header: String,
        @Path("movie_id") movieId: Long,
        @Query("append_to_response") appendToResponse: String = "account_states,credits"
    ): MovieResult
}