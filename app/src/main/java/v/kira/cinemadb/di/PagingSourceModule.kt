package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import v.kira.cinemadb.features.movies.MoviePagingSource
import v.kira.cinemadb.features.movies.MovieRemoteSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PagingSourceModule {

    @Provides
    @Singleton
    fun provideMoviePagingSource(
        movieRemoteSource: MovieRemoteSource
    ) = MoviePagingSource(remoteSource = movieRemoteSource)
}