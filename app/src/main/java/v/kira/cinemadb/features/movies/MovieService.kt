package v.kira.cinemadb.features.movies

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.ResponseObject

interface MovieService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<MovieResult>>>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<ResponseObject<List<MovieResult>>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<MovieResult>>>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<MovieResult>>>
}