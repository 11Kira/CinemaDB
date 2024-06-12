package v.kira.cinemadb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import v.kira.cinemadb.features.account.AccountRemoteSource
import v.kira.cinemadb.features.account.AccountRepository
import v.kira.cinemadb.features.movies.MovieRemoteSource
import v.kira.cinemadb.features.movies.MovieRepository
import v.kira.cinemadb.features.search.SearchRemoteSource
import v.kira.cinemadb.features.search.SearchRepository
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

    @Provides
    @Singleton
    fun provideAccountRepository(
        accountRemoteSource: AccountRemoteSource
    ) = AccountRepository(accountRemoteSource = accountRemoteSource)

    @Provides
    @Singleton
    fun provideSearchRepository(
        searchRemoteSource: SearchRemoteSource
    ) = SearchRepository(remoteSource = searchRemoteSource)
}