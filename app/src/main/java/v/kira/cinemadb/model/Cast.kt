package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class Cast (
    @SerializedName("id")
    val id: Long,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("character")
    val character: String,
    @SerializedName("cast_id")
    val castId: Long,
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("order")
    val order: Int,
)