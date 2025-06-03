package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.features.account.AccountRemoteSource
import com.kira.android.cinemadb.features.account.AccountRepository
import com.kira.android.cinemadb.features.movies.MovieRemoteSource
import com.kira.android.cinemadb.features.movies.MovieRepository
import com.kira.android.cinemadb.features.search.SearchRemoteSource
import com.kira.android.cinemadb.features.search.SearchRepository
import com.kira.android.cinemadb.features.tv.TVRemoteSource
import com.kira.android.cinemadb.features.tv.TVRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteSource: MovieRemoteSource
    ) = MovieRepository(remoteSource = movieRemoteSource)

    @Provides
    @Singleton
    fun provideTVRepository(
        tvRemoteSource: TVRemoteSource
    ) = TVRepository(remoteSource = tvRemoteSource)

    @Provides
    @Singleton
    fun provideAccountRepository(
        accountRemoteSource: AccountRemoteSource
    ) = AccountRepository(accountRemoteSource = accountRemoteSource)

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchRemoteSource: SearchRemoteSource
    ) = SearchRepository(remoteSource = searchRemoteSource)
}