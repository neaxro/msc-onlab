package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdCreateResponse
import com.example.msc_onlab.data.model.household.HouseholdDeleteResponse
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HouseholdApi {

    @Headers("Content-Type: application/json")
    @GET("/household/all/{user_id}")
    suspend fun getAllHouseholdsBrief(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String
    ): Response<HouseholdsBrief>

    @Headers("Content-Type: application/json")
    @PATCH("/household/id/{household_id}")
    suspend fun updateHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String,
        @Body newHouseholdData: HouseholdUpdateData
    ): Response<HouseholdUpdateResponse>

    @Headers("Content-Type: application/json")
    @POST("/household")
    suspend fun createHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body newHouseholdData: HouseholdCreateData
    ): Response<HouseholdCreateResponse>

    @Headers("Content-Type: application/json")
    @DELETE("/household/id/{household_id}")
    suspend fun deleteHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String
    ): Response<HouseholdDeleteResponse>
}
