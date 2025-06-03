package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.features.account.AccountRemoteSource
import com.kira.android.cinemadb.features.account.AccountService
import com.kira.android.cinemadb.features.movies.MovieRemoteSource
import com.kira.android.cinemadb.features.movies.MovieService
import com.kira.android.cinemadb.features.search.SearchRemoteSource
import com.kira.android.cinemadb.features.search.SearchService
import com.kira.android.cinemadb.features.tv.TVRemoteSource
import com.kira.android.cinemadb.features.tv.TVService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteSourceModule {

    @Provides
    @Singleton
    fun provideMovieRemoteSource(
        movieService: MovieService
    ) = MovieRemoteSource(movieService = movieService)

    @Provides
    @Singleton
    fun provideTVRemoteSource(
        tvService: TVService
    ) = TVRemoteSource(tvService = tvService)

    @Provides
    @Singleton
    fun provideAccountRemoteSource(
        accountService: AccountService
    ) = AccountRemoteSource(accountService = accountService)

    @Provides
    @Singleton
    fun provideSearchRemoteSource(
        searchService: SearchService
    ) = SearchRemoteSource(searchService = searchService)
}