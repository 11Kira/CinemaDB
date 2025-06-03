package com.kira.android.cinemadb.features.search

import com.kira.android.cinemadb.model.ListResponseObject
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

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