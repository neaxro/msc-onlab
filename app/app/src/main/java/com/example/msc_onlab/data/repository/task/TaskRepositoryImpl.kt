package com.example.msc_onlab.data.repository.task

import android.app.Application
import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskData
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskResponse
import com.example.msc_onlab.data.model.task.v2.update.UpdateTaskData
import com.example.msc_onlab.data.remote.TaskApi
import com.example.msc_onlab.domain.wrappers.Resource
import retrofit2.Response

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

    override suspend fun getTask(taskId: Int): Resource<GetTasksResponseItem> {
        val result = try{
            val response = api.getTask(taskId = taskId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched task!", data = response.body()!!)
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

    override suspend fun createTask(taskData: CreateTaskData): Resource<CreateTaskResponse> {
        val result = try{
            val response = api.createTask(task = taskData)

            // Check server response
            val res = if(response.code() == 201){
                Resource.Success(message = "Successfully created new task!", data = response.body()!!)
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

    override suspend fun updateTask(updateData: UpdateTaskData): Resource<Unit> {
        val result = try{
            val response = api.updateTask(updateData = updateData)

            // Check server response
            val res = if(response.code() == 204){
                Resource.Success(message = "Successfully updated task!", data = Unit)
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

    override suspend fun deleteTask(taskId: Int): Resource<Unit> {
        val result = try{
            val response = api.deleteTask(taskId = taskId)

            // Check server response
            val res = if(response.code() == 204){
                Resource.Success(message = "Successfully deleted task!", data = Unit)
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
