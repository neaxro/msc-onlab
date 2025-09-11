package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.profile.GetProfileResponse
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.data.model.profile.UpdateProfileResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileApi {

    @Headers("Content-Type: application/json")
    @GET("/auth/user/{user_id}")
    suspend fun getProfile(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String,
    ): Response<GetProfileResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/auth/user/{user_id}")
    suspend fun patchProfile(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String,
        @Body updateProfileData: UpdateProfileData
    ): Response<UpdateProfileResponse>
}