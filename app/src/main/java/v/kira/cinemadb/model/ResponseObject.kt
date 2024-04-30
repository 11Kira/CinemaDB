package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class ResponseObject (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<CinemaResult>?,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
)