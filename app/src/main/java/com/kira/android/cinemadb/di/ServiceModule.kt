package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.features.account.AccountService
import com.kira.android.cinemadb.features.movies.MovieService
import com.kira.android.cinemadb.features.search.SearchService
import com.kira.android.cinemadb.features.tv.TVService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Singleton
    @Provides
    fun provideTVService(retrofit: Retrofit): TVService =
        retrofit.create(TVService::class.java)

    @Singleton
    @Provides
    fun provideAccountService(retrofit: Retrofit): AccountService =
        retrofit.create(AccountService::class.java)

    @Singleton
    @Provides
    fun provideSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)
}