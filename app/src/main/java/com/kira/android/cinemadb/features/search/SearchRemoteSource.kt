package com.kira.android.cinemadb.features.search

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRemoteSource @Inject constructor(
    private val searchService: SearchService
) {
    suspend fun searchMovie(
        token: String,
        query: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        searchService.searchMovie(
            header = token,
            query = query,
            page = page
        )
    }

    suspend fun searchTVShow(
        token: String,
        query: String,
        page: Int
    ) = withContext(Dispatchers.IO) {
        searchService.searchTVShow(
            header = token,
            query = query,
            page = page
        )
    }
}