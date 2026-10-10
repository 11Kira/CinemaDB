package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.data.remote.service.AccountService
import com.kira.android.cinemadb.data.remote.service.MovieService
import com.kira.android.cinemadb.data.remote.service.SearchService
import com.kira.android.cinemadb.data.remote.service.TVService
import com.kira.android.cinemadb.data.remote.source.AccountRemoteSource
import com.kira.android.cinemadb.data.remote.source.SearchRemoteSource
import com.kira.android.cinemadb.data.remote.source.movies.MovieRemoteSource
import com.kira.android.cinemadb.data.remote.source.tv.TVRemoteSource
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