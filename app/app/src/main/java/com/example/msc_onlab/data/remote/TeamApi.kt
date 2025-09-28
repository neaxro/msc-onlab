package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.team.CreateTeamData
import com.example.msc_onlab.data.model.team.CreateTeamResponse
import com.example.msc_onlab.data.model.team.TeamDeleteResponse
import com.example.msc_onlab.data.model.team.TeamInfo
import com.example.msc_onlab.data.model.team.TeamMembers
import com.example.msc_onlab.data.model.team.TeamUpdate
import com.example.msc_onlab.data.model.team.TeamUpdateResponse
import com.example.msc_onlab.data.model.team.TeamsBrief
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

interface TeamApi {

    @Headers("Content-Type: application/json")
    @GET("/team/teams")
    suspend fun getAllTeams(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
    ): Response<TeamsBrief>

    @Headers("Content-Type: application/json")
    @GET("/team/teams/{id}/info")
    suspend fun getTeamInfo(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("id") teamId: Int,
    ): Response<TeamInfo>

    @Headers("Content-Type: application/json")
    @GET("/team/membership/{id}/members")
    suspend fun getTeamMembers(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("id") teamId: Int,
    ): Response<TeamMembers>

    @Headers("Content-Type: application/json")
    @POST("/team/teams")
    suspend fun createTeam(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body data: CreateTeamData
    ): Response<CreateTeamResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/team/teams/{id}")
    suspend fun updateTeam(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("id") id: Int,
        @Body teamUpdate: TeamUpdate
    ): Response<TeamUpdateResponse>

    @Headers("Content-Type: application/json")
    @DELETE("/team/teams/{id}")
    suspend fun deleteTeam(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("id") id: Int
    ): Response<TeamDeleteResponse>

    /* ------------------- */

    /*
    @Headers("Content-Type: application/json")
    @POST("/household")
    suspend fun createHousehold(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body newHouseholdData: HouseholdCreateData
    ): Response<HouseholdCreateResponse>

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
     */
}
