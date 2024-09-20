package com.example.msc_onlab.data.di

import com.example.msc_onlab.data.remote.LoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyApi(): LoginApi{
        return Retrofit.Builder()
            .baseUrl("http://test.com")
            .build()
            .create(LoginApi::class.java)
    }
}
