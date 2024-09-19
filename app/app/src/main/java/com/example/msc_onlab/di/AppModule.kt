package com.example.msc_onlab.di

import android.app.Application
import com.example.msc_onlab.data.remote.MyApi
import com.example.msc_onlab.data.repository.MyRepositoryImplementation
import com.example.msc_onlab.domain.repository.MyRepository
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
    fun provideMyApi(): MyApi{
        return Retrofit.Builder()
            .baseUrl("http://test.com")
            .build()
            .create(MyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyRepository(
        api: MyApi,
        app: Application
    ): MyRepository{
        return MyRepositoryImplementation(
            api = api,
            appContext = app
        )
    }
}