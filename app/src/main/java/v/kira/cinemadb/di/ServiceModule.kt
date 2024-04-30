package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import v.kira.cinemadb.features.movies.MovieAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun provideMovieAPI(retrofit: Retrofit): MovieAPI =
        retrofit.create(MovieAPI::class.java)
}