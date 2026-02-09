package com.kira.android.cinemadb.features.movies

import com.kira.android.cinemadb.model.AccountStates
import com.kira.android.cinemadb.model.ListResponseObject
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.UserReviewResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
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

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Header("Authorization") header: String,
        @Path("movie_id") movieId: Long,
        @Query("page") page: Int
    ): Response<ListResponseObject<List<UserReviewResult>>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") header: String,
        @Path("movie_id") movieId: Long,
        @Query("append_to_response") appendToResponse: String = "account_states,credits"
    ): MovieResult

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieWatchlistDetails(
        @Header("Authorization") header: String,
        @Path("movie_id") movieId: Long,
    ): AccountStates
}