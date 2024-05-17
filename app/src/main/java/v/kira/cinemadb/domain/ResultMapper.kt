package v.kira.cinemadb.domain

import retrofit2.Response
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.ResponseObject
import v.kira.cinemadb.model.TVShowResult

fun Response<ResponseObject<List<MovieResult>>>.mapMovieResultToDomain(): List<MovieResult> {
    return this.body()?.results?.map { movie ->
        MovieResult(
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

fun Response<ResponseObject<List<TVShowResult>>>.mapTVSeriesResultToDomain(): List<TVShowResult> {
    return this.body()?.results?.map { tv ->
        TVShowResult(
            id = tv.id,
            originalLanguage = tv.originalLanguage,
            name = tv.name,
            overview = tv.overview,
            posterPath = tv.posterPath,
            firstAirDate = tv.firstAirDate,
            originalName = tv.originalName
        )
    } ?: emptyList()
}

fun MovieResult.mapMovieDetailsToDomain(): MovieResult {
    return MovieResult(
        id = this.id,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title
    )
}

fun TVShowResult.mapTVShowDetailsToDomain(): TVShowResult {
    return TVShowResult(
        id = this.id,
        originalLanguage = this.originalLanguage,
        name = this.name,
        overview = this.overview,
        posterPath = this.posterPath,
        firstAirDate = this.firstAirDate,
        originalName = this.originalName
    )
}