package v.kira.cinemadb.features.tv

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import v.kira.cinemadb.model.ResponseObject
import v.kira.cinemadb.model.TVShowResult

interface TVService {
    @GET("tv/airing_today")
    suspend fun getAiringTodayTVShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVShowResult>>>

    @GET("trending/tv/week")
    suspend fun getTrendingTVShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVShowResult>>>

    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Header("Authorization") header: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<ResponseObject<List<TVShowResult>>>

    @GET("tv/{series_id}")
    suspend fun getTVShowDetails(
        @Header("Authorization") header: String,
        @Path("series_id") seriesId: Long,
        @Query("language") language: String,
    ): TVShowResult
}