package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import v.kira.cinemadb.features.movies.MovieRemoteSource
import v.kira.cinemadb.features.movies.MovieService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteSourceModule {

    @Provides
    @Singleton
    fun provideMovieRemoteSource(
        movieService: MovieService
    ) = MovieRemoteSource(movieService = movieService)
}