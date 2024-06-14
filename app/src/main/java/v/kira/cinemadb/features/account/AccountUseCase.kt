package v.kira.cinemadb.features.account

import androidx.paging.PagingData
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import v.kira.cinemadb.model.Account
import v.kira.cinemadb.model.MovieResult
import v.kira.cinemadb.model.ResponseObject
import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun getAccountDetails(
        token: String,
        accountId: Long
    ): Account {
        return accountRepository.getAccountDetails(token, accountId)
    }

    suspend fun addToWatchlist(
        token: String,
        accountId: Long,
        body: JsonObject
    ): ResponseObject {
        return accountRepository.addToWatchlist(token, accountId, body)
    }

    fun getMovieWatchlist(token: String, accountId: Long): Flow<PagingData<MovieResult>> {
        return accountRepository.getMovieWatchlist(token, accountId)
    }

    fun getTVShowWatchlist(token: String, accountId: Long): Flow<PagingData<TVShowResult>> {
        return accountRepository.getTVShowWatchlist(token, accountId)
    }
}