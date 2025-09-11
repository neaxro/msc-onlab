package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TaskApi {

    @Headers("Content-Type: application/json")
    @GET("/task/tasks")
    suspend fun getAllTasks(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Query("teamId") teamId: Int
    ): Response<TasksResponse>
}
