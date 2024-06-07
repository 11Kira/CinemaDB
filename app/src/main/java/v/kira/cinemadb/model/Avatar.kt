package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class Avatar (
    @SerializedName("tmdb")
    val tmdb: TMDB
)