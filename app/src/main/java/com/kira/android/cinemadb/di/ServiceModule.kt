package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.data.remote.service.AccountService
import com.kira.android.cinemadb.data.remote.service.MovieService
import com.kira.android.cinemadb.data.remote.service.SearchService
import com.kira.android.cinemadb.data.remote.service.TVService
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