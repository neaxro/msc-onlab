package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface LoginApi {

    @POST("/auth/login")
    suspend fun login(
        @Body loginData: LoginData
    ): Response<LoginResponse>
}
