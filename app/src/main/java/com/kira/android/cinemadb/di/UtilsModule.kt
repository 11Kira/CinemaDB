package com.kira.android.cinemadb.di

import android.content.Context
import com.kira.android.cinemadb.utils.NetworkUtils
import com.kira.android.cinemadb.utils.SettingsPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideSettingsPrefs(
        @ApplicationContext context: Context,
    ): SettingsPrefs = SettingsPrefs(context)

    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils = NetworkUtils()
}
