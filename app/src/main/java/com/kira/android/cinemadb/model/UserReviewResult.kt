package com.kira.android.cinemadb.model

import com.google.gson.annotations.SerializedName

data class UserReviewResult(
    @SerializedName("id")
    val id: Long,
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: AuthorDetails,
    @SerializedName("content")
    val content: String,
)

data class AuthorDetails(
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_path")
    val avatarPath: String,
    @SerializedName("rating")
    val rating: Double
)
