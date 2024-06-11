package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class AccountStates(
    @SerializedName("favorite")
    val favorite: Boolean,
    @SerializedName("rated")
    val rated: Boolean,
    @SerializedName("watchlist")
    val watchlist: Boolean,
)