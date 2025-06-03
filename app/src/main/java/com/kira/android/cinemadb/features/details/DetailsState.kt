package com.kira.android.cinemadb.features.details

import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult

sealed class DetailsState {
    data class ShowError(val error: Any): DetailsState()
    data class SetMovieDetails(val movieDetails: MovieResult): DetailsState()
    data class SetTvShowDetails(val tvShowDetails: TVShowResult): DetailsState()
}