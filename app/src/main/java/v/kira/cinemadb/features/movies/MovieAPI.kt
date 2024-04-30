package v.kira.cinemadb.features.movies

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import v.kira.cinemadb.model.ResponseObject

interface MovieAPI {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Response<ResponseObject>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject>
}