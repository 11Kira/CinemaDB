package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class MovieResult (
    @SerializedName("id")
    val id: Long,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("runtime")
    val runtime: Int
)