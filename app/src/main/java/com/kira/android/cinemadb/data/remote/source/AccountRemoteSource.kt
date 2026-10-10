package com.kira.android.cinemadb.data.remote.source

import com.google.gson.JsonObject
import com.kira.android.cinemadb.data.remote.service.AccountService
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
        page: Int
    ) = withContext(Dispatchers.IO) {
        accountService.getMovieWatchlist(
            header = token,
            accountId = accountId,
            page = page
        )
    }

    suspend fun getTVShowWatchlist(
        token: String,
        accountId: Long,
        page: Int
    ) = withContext(Dispatchers.IO) {
        accountService.getTVShowWatchlist(
            header = token,
            accountId = accountId,
            page = page
        )
    }
}