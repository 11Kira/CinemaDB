package com.kira.android.cinemadb.features.account

import com.google.gson.JsonObject
import com.kira.android.cinemadb.model.Account
import com.kira.android.cinemadb.model.ListResponseObject
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.ResponseObject
import com.kira.android.cinemadb.model.TVShowResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountService {
    @GET("account/{account_id}")
    suspend fun getAccountDetails(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long
    ): Account

    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long,
        @Body body: JsonObject
    ): ResponseObject

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getMovieWatchlist(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<MovieResult>>>

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getTVShowWatchlist(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<TVShowResult>>>
}