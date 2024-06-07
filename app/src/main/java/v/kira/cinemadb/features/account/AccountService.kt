package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import v.kira.cinemadb.model.Account
import v.kira.cinemadb.model.ResponseObject

interface AccountService {
    @GET("account/{account_id}")
    suspend fun getAccountDetails(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long
    ): Account

    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
        @Header("Authorization") header: String,
        @Path("account_id") accountId: Long,
        @Body body: JsonObject
    ): ResponseObject
}