package com.kira.android.cinemadb.domain

import com.kira.android.cinemadb.model.ListResponseObject
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult
import retrofit2.Response

fun Response<ListResponseObject<List<MovieResult>>>.mapMovieResultToDomain(): List<MovieResult> {
    return this.body()?.results?.map { movie ->
        MovieResult(
            id = movie.id,
            originalLanguage = movie.originalLanguage,
            originalTitle = movie.originalTitle,
            overview = movie.overview,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            title = movie.title,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            tagline = movie.tagline,
            status = movie.status,
            runtime = movie.runtime.or(0),
            genres = movie.genres,
            originCountry = movie.originCountry,
            credits = movie.credits,
            accountStates = movie.accountStates
        )
    } ?: emptyList()
}

fun Response<ListResponseObject<List<TVShowResult>>>.mapTVSeriesResultToDomain(): List<TVShowResult> {
    return this.body()?.results?.map { tv ->
        TVShowResult(
            id = tv.id,
            originalLanguage = tv.originalLanguage,
            name = tv.name,
            overview = tv.overview,
            posterPath = tv.posterPath,
            originalName = tv.originalName,
            numberOfEpisodes = tv.numberOfEpisodes.or(0),
            numberOfSeasons = tv.numberOfSeasons.or(0),
            voteAverage = tv.voteAverage,
            voteCount = tv.voteCount,
            tagline = tv.tagline,
            status = tv.status,
            genres = tv.genres,
            originCountry = tv.originCountry,
            firstAirDate = tv.firstAirDate,
            lastAirDate = tv.lastAirDate,
            credits = tv.credits,
            accountStates = tv.accountStates
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
        title = this.title,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        tagline = this.tagline,
        status = this.status,
        runtime = this.runtime,
        genres = this.genres,
        originCountry = this.originCountry,
        credits = this.credits,
        accountStates = this.accountStates
    )
}

fun TVShowResult.mapTVShowDetailsToDomain(): TVShowResult {
    return TVShowResult(
        id = this.id,
        originalLanguage = this.originalLanguage,
        name = this.name,
        overview = this.overview,
        posterPath = this.posterPath,
        originalName = this.originalName,
        numberOfEpisodes = this.numberOfEpisodes,
        numberOfSeasons = this.numberOfSeasons,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        tagline = this.tagline,
        status = this.status,
        genres = this.genres,
        originCountry = this.originCountry,
        firstAirDate = this.firstAirDate,
        lastAirDate = this.lastAirDate,
        credits = this.credits,
        accountStates = this.accountStates
    )
}