package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import v.kira.cinemadb.features.movies.MovieRemoteSource
import v.kira.cinemadb.features.movies.MovieRepository
import v.kira.cinemadb.features.tv.TVRemoteSource
import v.kira.cinemadb.features.tv.TVRepository
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
}