package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRemoteSource @Inject constructor(
    private val accountService: AccountService
) {
    suspend fun getAccountDetails(
        token: String,
        accountId: Long,
    ) = withContext(Dispatchers.IO) {
        accountService.getAccountDetails(
            header = token,
            accountId = accountId
        )
    }

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

    suspend fun getMovieWatchlist(
        token: String,
        accountId: Long,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        accountService.getMovieWatchlist(
            header = token,
            language = language,
            accountId = accountId,
            page = page
        )
    }

    suspend fun getTVShowWatchlist(
        token: String,
        accountId: Long,
        language: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        accountService.getTVShowWatchlist(
            header = token,
            language = language,
            accountId = accountId,
            page = page
        )
    }
}