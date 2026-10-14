package com.kira.android.cinemadb.features.details

import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.TVShowResult

data class DetailsUiState(
    val movieDetails: MovieResult? = null,
    val tvShowDetails: TVShowResult? = null,
    val isLoading: Boolean = false
)

sealed interface DetailsUiEffect {
    data class ShowSnackbar(val message: String) : DetailsUiEffect
}