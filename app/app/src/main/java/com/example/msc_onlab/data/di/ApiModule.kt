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
object ApiModule {

    private const val BASE_URL: String = "http://192.168.1.68:5000"

    @Provides
    @Singleton
    fun provideMyApi(): LoginApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(LoginApi::class.java)
    }
}
