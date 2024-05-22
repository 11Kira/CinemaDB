package v.kira.cinemadb.features.details

import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.TVShowResult

sealed class DetailsState {
    data class ShowError(val error: Any): DetailsState()
    data class SetMovieDetails(val movieDetails: MovieResult): DetailsState()
    data class SetTvShowDetails(val tvShowDetails: TVShowResult): DetailsState()
}