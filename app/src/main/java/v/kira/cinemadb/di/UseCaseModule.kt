package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import v.kira.cinemadb.features.details.DetailsRepository
import v.kira.cinemadb.features.details.DetailsUseCase
import v.kira.cinemadb.features.movies.MovieRepository
import v.kira.cinemadb.features.movies.MovieUseCase
import v.kira.cinemadb.features.tv.TVRepository
import v.kira.cinemadb.features.tv.TVUseCase

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
        repository: DetailsRepository
    ) = DetailsUseCase(repository)
}