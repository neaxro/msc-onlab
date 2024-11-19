package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.profile.GetProfileResponse
import com.example.msc_onlab.data.model.profile.ProfileResponse
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.data.model.task.patch.TaskPatchData
import com.example.msc_onlab.data.model.task.patch.TaskPatchResponse
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
    @GET("/user/{user_id}")
    suspend fun getProfile(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String,
    ): Response<GetProfileResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/user")
    suspend fun patchProfile(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body updateProfileData: UpdateProfileData
    ): Response<ProfileResponse>
}