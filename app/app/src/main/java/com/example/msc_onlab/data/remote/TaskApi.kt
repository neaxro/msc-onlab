package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskData
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskResponse
import com.example.msc_onlab.data.model.task.v2.update.UpdateTaskData
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {

    @Headers("Content-Type: application/json")
    @GET("/task/tasks")
    suspend fun getAllTasks(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Query("teamId") teamId: Int
    ): Response<TasksResponse>

    @Headers("Content-Type: application/json")
    @GET("/task/tasks/{task_id}")
    suspend fun getTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("task_id") taskId: Int
    ): Response<GetTasksResponseItem>

    @Headers("Content-Type: application/json")
    @POST("/task/tasks")
    suspend fun createTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body task: CreateTaskData
    ): Response<CreateTaskResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/task/tasks")
    suspend fun updateTask(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body updateData: UpdateTaskData
    ): Response<Unit>
}
