package com.example.msc_onlab.data.repository.household

import android.app.Application
import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdCreateResponse
import com.example.msc_onlab.data.model.household.HouseholdDeleteResponse
import com.example.msc_onlab.data.model.household.HouseholdDetailedResponse
import com.example.msc_onlab.data.model.household.HouseholdMembersResponse
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateResponse
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.model.members.MembersResponse
import com.example.msc_onlab.data.model.task.TaskDeleteResponse
import com.example.msc_onlab.data.model.task.TaskResponse
import com.example.msc_onlab.data.model.task.patch.TaskPatchData
import com.example.msc_onlab.data.model.task.patch.TaskPatchResponse
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.domain.wrappers.Resource
import retrofit2.Response

class HouseholdRepositoryImpl(
    private val api: HouseholdApi,
    private val app: Application
) : HouseholdRepository {
    private val context = app.applicationContext

    override suspend fun getAllHouseholds(userId: String): Resource<HouseholdsBrief> {

        val result = try{
            val response = api.getAllHouseholdsBrief(userId = userId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched all households!", data = response.body()!!)
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

    override suspend fun updateHousehold(
        householdId: String,
        newHouseholdData: HouseholdUpdateData
    ): Resource<HouseholdUpdateResponse> {

        val result = try{
            val response = api.updateHousehold(householdId = householdId, newHouseholdData = newHouseholdData)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully modified household!", data = response.body()!!)
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

    override suspend fun createHousehold(
        newHouseholdData: HouseholdCreateData
    ): Resource<HouseholdCreateResponse> {

        val result = try{
            val response = api.createHousehold(newHouseholdData = newHouseholdData)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully created household!", data = response.body()!!)
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

    override suspend fun deleteHousehold(
        householdId: String
    ): Resource<HouseholdDeleteResponse> {
        val result = try{
            val response = api.deleteHousehold(householdId = householdId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully deleted household!", data = response.body()!!)
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

    override suspend fun getHouseholdById(householdId: String): Resource<HouseholdDetailedResponse> {
        val result = try{
            val response = api.getHouseholdById(householdId = householdId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched household!", data = response.body()!!)
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

    override suspend fun getUsers(householdId: String): Resource<HouseholdMembersResponse> {
        val result = try{
            val response = api.getUsersInHousehold(householdId = householdId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched users in household!", data = response.body()!!)
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

    override suspend fun getTasks(householdId: String): Resource<HouseholdTasksResponse> {
        val result = try{
            val response = api.getTasksInHousehold(householdId = householdId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched tasks in household!", data = response.body()!!)
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

    override suspend fun deleteTask(
        householdId: String,
        taskId: String
    ): Resource<TaskDeleteResponse> {
        val result = try{
            val response = api.deleteTask(householdId = householdId, taskId = taskId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully task task!", data = response.body()!!)
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

    override suspend fun getMembers(householdId: String): Resource<MembersResponse> {
        val result = try{
            val response = api.getMembers(householdId = householdId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully get all members!", data = response.body()!!)
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

    override suspend fun getTaskById(taskId: String): Resource<TaskResponse> {
        val result = try{
            val response = api.getTaskById(taskId = taskId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully get task!", data = response.body()!!)
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

    override suspend fun patchTask(
        householdId: String,
        taskId: String,
        newTaskData: TaskPatchData
    ): Resource<TaskPatchResponse> {
        val result = try{
            val response = api.patchTask(
                householdId = householdId,
                taskId = taskId,
                newTaskData = newTaskData
            )

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully updated task!", data = response.body()!!)
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
