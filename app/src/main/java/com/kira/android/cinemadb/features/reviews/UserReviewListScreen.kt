package com.kira.android.cinemadb.features.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kira.android.cinemadb.R
import com.kira.android.cinemadb.features.details.DynamicStarRating
import com.kira.android.cinemadb.model.UserReviewResult
import kotlin.math.roundToInt

lateinit var viewModel: UserReviewViewModel

@Composable
fun UserReviewListScreen(
    id: Long,
    type: Int
) {
    viewModel = hiltViewModel()
    MainScreen(type)
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
fun MainScreen(type: Int) {
    val movieReviewlist by rememberUpdatedState(newValue = viewModel.movieReviewPagingState.collectAsLazyPagingItems())
    val tvShowReviewlist by rememberUpdatedState(newValue = viewModel.tvShowReviewPagingState.collectAsLazyPagingItems())
    if (type == 1) {
        PopulateReviewList(movieReviewlist)
    } else {
        PopulateReviewList(tvShowReviewlist)
    }
}

@Composable
fun PopulateReviewList(
    movieReviews: LazyPagingItems<UserReviewResult>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movieReviews.itemCount) { index ->
            val selectedReview = movieReviews[index]
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = selectedReview?.author.toString(),
                            fontSize = 17.sp,
                            fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            selectedReview?.authorDetails?.rating?.let { rating ->
                                DynamicStarRating(rating = rating)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${rating.times(10.0).roundToInt() / 10.0}/10",
                                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = selectedReview?.content.toString(),
                            fontSize = 15.sp,
                            fontFamily = Font(R.font.roboto_regular).toFontFamily(),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}