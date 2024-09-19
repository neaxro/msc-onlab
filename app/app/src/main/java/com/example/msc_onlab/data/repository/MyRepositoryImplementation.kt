package com.example.msc_onlab.data.repository

import android.app.Application
import com.example.msc_onlab.R
import com.example.msc_onlab.data.remote.MyApi
import com.example.msc_onlab.domain.repository.MyRepository

class MyRepositoryImplementation(
    private val api: MyApi,
    private val appContext: Application,
): MyRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        println("Hello from repository, app name is $appName")
    }

    override suspend fun doNetworkCall() {
        TODO("Not yet implemented")
    }
}