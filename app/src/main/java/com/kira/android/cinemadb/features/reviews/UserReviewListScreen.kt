package com.kira.android.cinemadb.features.reviews

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner

lateinit var viewModel: UserReviewViewModel

@Composable
fun UserReviewListScreen(
    id: Long,
    type: Int
) {
    viewModel = hiltViewModel()
    MainScreen()
    when (type) {
        1 -> {
            viewModel.getMovieReviews(id)
        }

        2 -> {
            viewModel.getTVShowReviews(id)
        }
    }
}

@Composable
fun MainScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
}