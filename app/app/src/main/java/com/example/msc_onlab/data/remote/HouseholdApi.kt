package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdCreateResponse
import com.example.msc_onlab.data.model.household.HouseholdDeleteResponse
import com.example.msc_onlab.data.model.household.HouseholdDetailedResponse
import com.example.msc_onlab.data.model.household.HouseholdMembersResponse
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateResponse
import com.example.msc_onlab.data.model.members.MembersResponse
import com.example.msc_onlab.data.model.task.TaskDeleteResponse
import com.example.msc_onlab.data.model.task.TaskResponse
import com.example.msc_onlab.data.model.task.create.CreateTaskData
import com.example.msc_onlab.data.model.task.create.CreateTaskResponse
import com.example.msc_onlab.data.model.task.patch.TaskPatchData
import com.example.msc_onlab.data.model.task.patch.TaskPatchResponse
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

    @Headers("Content-Type: application/json")
    @GET("/household/id/{household_id}/detailed")
    suspend fun getHouseholdById(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String
    ): Response<HouseholdDetailedResponse>

    @Headers("Content-Type: application/json")
    @GET("/household/id/{household_id}/users")
    suspend fun getUsersInHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String
    ): Response<HouseholdMembersResponse>

    @Headers("Content-Type: application/json")
    @GET("/household/id/{household_id}/tasks/all/detailed")
    suspend fun getTasksInHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String
    ): Response<HouseholdTasksResponse>

    @Headers("Content-Type: application/json")
    @DELETE("/household/id/{household_id}/tasks/id/{task_id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String,
        @Path("task_id") taskId: String,
    ): Response<TaskDeleteResponse>

    @Headers("Content-Type: application/json")
    @GET("/household/id/{household_id}/users")
    suspend fun getMembers(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String,
    ): Response<MembersResponse>

    @Headers("Content-Type: application/json")
    @GET("/household/tasks/id/{task_id}/detailed")
    suspend fun getTaskById(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("task_id") taskId: String,
    ): Response<TaskResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/household/id/{household_id}/tasks/id/{task_id}")
    suspend fun patchTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String,
        @Path("task_id") taskId: String,
        @Body newTaskData: TaskPatchData
    ): Response<TaskPatchResponse>

    @Headers("Content-Type: application/json")
    @POST("/household/id/{household_id}/tasks")
    suspend fun createTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("household_id") householdId: String,
        @Body taskData: CreateTaskData
    ): Response<CreateTaskResponse>
}
