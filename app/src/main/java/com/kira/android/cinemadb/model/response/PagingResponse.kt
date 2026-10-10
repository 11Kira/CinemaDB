package com.kira.android.cinemadb.model.response

import com.google.gson.annotations.SerializedName

data class PagingResponse<T>(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: T,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
)