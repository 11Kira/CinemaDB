package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRemoteSource @Inject constructor(
    private val accountService: AccountService
) {
    suspend fun addToWatchlist(
        token: String,
        accountId: Long,
        body: JsonObject
    ) = withContext(Dispatchers.IO) {
        accountService.addToWatchlist(
            header = token,
            accountId = accountId,
            body = body
        )
    }
}