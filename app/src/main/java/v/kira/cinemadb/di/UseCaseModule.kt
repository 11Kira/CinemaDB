package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import v.kira.cinemadb.features.movies.MovieRepository
import v.kira.cinemadb.features.movies.MovieUseCase

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideMovieUseCase(
        repository: MovieRepository
    ) = MovieUseCase(repository)
}