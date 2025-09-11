package com.example.msc_onlab.data.repository.task

import android.app.Application
import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.data.remote.TaskApi
import com.example.msc_onlab.domain.wrappers.Resource

class TaskRepositoryImpl(
    private val api: TaskApi,
    private val app: Application
) : TaskRepository {
    override suspend fun getTasks(teamId: Int): Resource<TasksResponse> {
        val result = try{
            val response = api.getAllTasks(teamId = teamId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched all tasks!", data = response.body()!!)
            }
            else{
                // Server error
                Resource.Error(message = response.errorBody()!!.string())
            }

            res
        } catch (e: Exception){
            // Network error
            Resource.Error("Network error occurred.")
        }

        return result
    }
}
