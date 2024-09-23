package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.register.RegisterData
import com.example.msc_onlab.data.model.register.RegisterResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface HouseholdApi {

    @Headers("Content-Type: application/json")
    @GET("/household/all/{user_id}")
    suspend fun getAllHouseholdsBrief(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String
    ): Response<HouseholdsBrief>
}
