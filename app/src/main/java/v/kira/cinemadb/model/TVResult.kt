package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class TVResult (
    @SerializedName("id")
    val id: Long,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("name")
    val name: String,
)