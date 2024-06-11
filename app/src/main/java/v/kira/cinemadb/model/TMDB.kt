package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class TMDB(
    @SerializedName("avatar_path")
    val avatarPath: String
)