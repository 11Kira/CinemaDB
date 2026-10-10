package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.data.remote.source.AccountRemoteSource
import com.kira.android.cinemadb.data.remote.source.MovieRemoteSource
import com.kira.android.cinemadb.data.remote.source.SearchRemoteSource
import com.kira.android.cinemadb.data.remote.source.TVRemoteSource
import com.kira.android.cinemadb.data.repository.AccountRepository
import com.kira.android.cinemadb.data.repository.MovieRepository
import com.kira.android.cinemadb.data.repository.SearchRepository
import com.kira.android.cinemadb.data.repository.TVRepository
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