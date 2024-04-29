package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class CinemaResult (
    @SerializedName("id")
    val id: Long,
    @SerializedName("original_language")
    val originalLanguage: Long,
    @SerializedName("original_title")
    val originalTitle: Long,
    @SerializedName("overview")
    val overview: Long,
    @SerializedName("poster_path")
    val posterPath: Long,
    @SerializedName("release_date")
    val releaseDate: Long,
    @SerializedName("title")
    val title: String,
)