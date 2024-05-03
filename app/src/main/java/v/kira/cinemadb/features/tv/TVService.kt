package v.kira.cinemadb.features.tv

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import v.kira.cinemadb.model.ResponseObject
import v.kira.cinemadb.model.TVResult

interface TVService {
    @GET("tv/airing_today")
    suspend fun getAiringTodayTvShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVResult>>>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVResult>>>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVResult>>>
}