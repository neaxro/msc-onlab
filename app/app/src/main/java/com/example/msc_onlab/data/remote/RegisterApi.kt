package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.model.register.RegisterData
import com.example.msc_onlab.data.model.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterApi {

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    suspend fun register(
        @Body registerData: RegisterData
    ): Response<RegisterResponse>
}
