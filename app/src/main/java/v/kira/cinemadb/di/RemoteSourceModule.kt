package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import v.kira.cinemadb.features.details.DetailsRemoteSource
import v.kira.cinemadb.features.details.DetailsService
import v.kira.cinemadb.features.movies.MovieRemoteSource
import v.kira.cinemadb.features.movies.MovieService
import v.kira.cinemadb.features.tv.TVRemoteSource
import v.kira.cinemadb.features.tv.TVService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteSourceModule {

    @Provides
    @Singleton
    fun provideMovieRemoteSource(
        movieService: MovieService
    ) = MovieRemoteSource(movieService = movieService)

    @Provides
    @Singleton
    fun provideTVRemoteSource(
        tvService: TVService
    ) = TVRemoteSource(tvService = tvService)

    @Provides
    @Singleton
    fun provideDetailsRemoteSource(
        detailsService: DetailsService
    ) = DetailsRemoteSource(detailsService = detailsService)
}