package com.kira.android.cinemadb.model

import com.google.gson.annotations.SerializedName

data class UserReviewResult(
    @SerializedName("id")
    val id: Long,
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("rating")
    val rating: Double
)
