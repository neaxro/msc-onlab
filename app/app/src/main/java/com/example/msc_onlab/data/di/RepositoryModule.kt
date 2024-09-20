package com.example.msc_onlab.data.di

import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.data.repository.LoginRepository
import com.example.msc_onlab.data.repository.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginApi: LoginApi
    ): LoginRepository{
        return LoginRepositoryImpl(
            loginApi = loginApi
        )
    }
}
