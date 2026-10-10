package com.kira.android.cinemadb.di

import com.kira.android.cinemadb.data.repository.AccountRepository
import com.kira.android.cinemadb.data.repository.MovieRepository
import com.kira.android.cinemadb.data.repository.TVRepository
import com.kira.android.cinemadb.domain.AccountUseCase
import com.kira.android.cinemadb.domain.DetailsUseCase
import com.kira.android.cinemadb.domain.MovieUseCase
import com.kira.android.cinemadb.domain.TVUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideMovieUseCase(
        repository: MovieRepository
    ) = MovieUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideTVUseCase(
        repository: TVRepository
    ) = TVUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideDetailsUseCase(
        movieRepository: MovieRepository,
        tvRepository: TVRepository
    ) = DetailsUseCase(movieRepository, tvRepository)

    @Provides
    @ViewModelScoped
    fun provideAccountUseCase(
        repository: AccountRepository
    ) = AccountUseCase(repository)
}