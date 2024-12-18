package com.example.msc_onlab.data.di

import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.InvitationApi
import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.data.remote.ProfileApi
import com.example.msc_onlab.data.remote.RegisterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL: String = "http://192.168.1.68:5000"

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterApi(): RegisterApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RegisterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHouseholdApi(): HouseholdApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(HouseholdApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(): ProfileApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInvitationApi(): InvitationApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(InvitationApi::class.java)
    }
}
