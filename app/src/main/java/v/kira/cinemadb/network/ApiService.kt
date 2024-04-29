package v.kira.cinemadb.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import v.kira.cinemadb.model.CinemaResult

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<List<CinemaResult>>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<List<CinemaResult>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<List<CinemaResult>>
}