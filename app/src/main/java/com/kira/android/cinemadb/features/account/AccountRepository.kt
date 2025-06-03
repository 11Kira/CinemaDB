package com.kira.android.cinemadb.features.account

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.JsonObject
import com.kira.android.cinemadb.features.account.watchlist.MovieWatchlistPagingSource
import com.kira.android.cinemadb.features.account.watchlist.TVShowWatchlistPagingSource
import com.kira.android.cinemadb.model.Account
import com.kira.android.cinemadb.model.MovieResult
import com.kira.android.cinemadb.model.ResponseObject
import com.kira.android.cinemadb.model.TVShowResult
import kotlinx.coroutines.flow.Flow
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

    fun getMovieWatchlist(token: String, accountId: Long): Flow<PagingData<MovieResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            MovieWatchlistPagingSource(token, accountId, accountRemoteSource)
        }.flow

    fun getTVShowWatchlist(token: String, accountId: Long): Flow<PagingData<TVShowResult>> =
        Pager(PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = false)) {
            TVShowWatchlistPagingSource(token, accountId, accountRemoteSource)
        }.flow
}