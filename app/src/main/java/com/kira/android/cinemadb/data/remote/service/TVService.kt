package com.kira.android.cinemadb.data.remote.service

import com.kira.android.cinemadb.model.AccountStates
import com.kira.android.cinemadb.model.TVShowResult
import com.kira.android.cinemadb.model.UserReviewResult
import com.kira.android.cinemadb.model.response.PagingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TVService {
    @GET("trending/tv/week")
    suspend fun getTrendingTVShows(
        @Header("Authorization") header: String,
        @Query("page") page: Int
    ): Response<PagingResponse<List<TVShowResult>>>

    @GET("tv/top_rated")
    suspend fun getTopRatedTVShows(
        @Header("Authorization") header: String,
        @Query("page") page: Int
    ): Response<PagingResponse<List<TVShowResult>>>

    @GET("tv/{series_id}/reviews")
    suspend fun getTVShowReviews(
        @Header("Authorization") header: String,
        @Path("series_id") seriesId: Long,
        @Query("page") page: Int
    ): Response<PagingResponse<List<UserReviewResult>>>

    @GET("tv/{series_id}")
    suspend fun getTVShowDetails(
        @Header("Authorization") header: String,
        @Path("series_id") seriesId: Long,
        @Query("append_to_response") appendToResponse: String = "account_states,credits"
    ): TVShowResult

    @GET("tv/{series_id}/account_states")
    suspend fun getTVShowWatchlistDetails(
        @Header("Authorization") header: String,
        @Path("series_id") seriesId: Long,
    ): AccountStates
}