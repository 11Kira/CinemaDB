package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import v.kira.cinemadb.model.Account
import v.kira.cinemadb.model.ResponseObject
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountRemoteSource: AccountRemoteSource
) {
    suspend fun getAccountDetails(token: String, accountId: Long): Account {
        return accountRemoteSource.getAccountDetails(token, accountId = accountId)
    }
    suspend fun addToWatchlist(token: String, accountId: Long, body: JsonObject): ResponseObject {
        return accountRemoteSource.addToWatchlist(token, accountId = accountId, body = body)
    }
}