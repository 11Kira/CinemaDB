package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import v.kira.cinemadb.model.ResponseObject

interface AccountService {
    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long,
        @Body body: JsonObject
    ): ResponseObject
}