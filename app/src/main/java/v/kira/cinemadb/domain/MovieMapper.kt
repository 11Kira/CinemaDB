package v.kira.cinemadb.domain

import retrofit2.Response
import v.kira.cinemadb.model.CinemaResult
import v.kira.cinemadb.model.ResponseObject

fun Response<ResponseObject>.mapMovieResultToDomain(): List<CinemaResult> {
    return this.body()?.results?.map { movie ->
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