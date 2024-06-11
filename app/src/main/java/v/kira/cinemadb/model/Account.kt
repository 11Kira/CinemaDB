package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class Account (
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("id")
    val id: Long,
    @SerializedName("iso_3166_1")
    val iso3166_1: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("include_adult")
    val includeAdult: Boolean,
    @SerializedName("username")
    val username: String,
)