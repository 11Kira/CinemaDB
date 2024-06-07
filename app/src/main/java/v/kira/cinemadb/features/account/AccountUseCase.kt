package v.kira.cinemadb.features.account

import com.google.gson.JsonObject
import v.kira.cinemadb.model.ResponseObject
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun addToWatchlist(
        token: String,
        accountId: Long,
        body: JsonObject
    ): ResponseObject {
        return accountRepository.addToWatchlist(token, accountId, body)
    }
}