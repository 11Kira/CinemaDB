package v.kira.cinemadb.domain

import retrofit2.Response
import v.kira.cinemadb.model.CinemaResult

fun Response<List<CinemaResult>>.mapMovieResultToDomain(): List<CinemaResult> {
    return this.body()?.map { movie ->
        CinemaResult(
            id = movie.id,
            originalLanguage = movie.originalLanguage,
            originalTitle = movie.originalTitle,
            overview = movie.overview,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            title = movie.title
        )
    } ?: emptyList()
}