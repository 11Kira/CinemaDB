package v.kira.cinemadb.model

import com.google.gson.annotations.SerializedName

data class ResponseObject (
    @SerializedName("success") val success: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)