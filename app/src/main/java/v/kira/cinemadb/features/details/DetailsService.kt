package v.kira.cinemadb.features.details

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import v.kira.cinemadb.model.MovieResult

interface DetailsService {
    @GET("movie/now_playing")
    suspend fun getMovieDetails(
        @Header("Authorization") header: String,
        @Path("movie_id") movieId: Long,
        @Query("language") language: String,
    ): MovieResult
}