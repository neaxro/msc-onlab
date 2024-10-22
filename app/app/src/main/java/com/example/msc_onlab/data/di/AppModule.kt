package com.example.msc_onlab.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.msc_onlab.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val filename = "thingsToRemember"
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    }
}
