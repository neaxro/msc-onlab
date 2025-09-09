package com.example.msc_onlab.data.repository.team

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
import com.example.msc_onlab.data.model.members.MembersResponse
import com.example.msc_onlab.data.model.task.TaskDeleteResponse
import com.example.msc_onlab.data.model.task.TaskResponse
import com.example.msc_onlab.data.model.task.create.CreateTaskData
import com.example.msc_onlab.data.model.task.create.CreateTaskResponse
import com.example.msc_onlab.data.model.task.patch.TaskPatchData
import com.example.msc_onlab.data.model.task.patch.TaskPatchResponse
import com.example.msc_onlab.data.model.team.TeamDeleteResponse
import com.example.msc_onlab.data.model.team.TeamInfo
import com.example.msc_onlab.data.model.team.TeamMembers
import com.example.msc_onlab.data.model.team.TeamUpdate
import com.example.msc_onlab.data.model.team.TeamUpdateResponse
import com.example.msc_onlab.data.model.team.TeamsBrief
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.TeamApi
import com.example.msc_onlab.domain.wrappers.Resource

class TeamRepositoryImpl(
    private val api: TeamApi,
    private val app: Application
) : TeamRepository {
    private val context = app.applicationContext
    override suspend fun getAllTeams(): Resource<TeamsBrief> {
        val result = try{
            val response = api.getAllTeams()

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched all teams!", data = response.body()!!)
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

    override suspend fun getTeamInfo(teamId: Int): Resource<TeamInfo> {
        val result = try{
            val response = api.getTeamInfo(teamId = teamId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched team info!", data = response.body()!!)
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

    override suspend fun getTeamMembers(teamId: Int): Resource<TeamMembers> {
        val result = try{
            val response = api.getTeamMembers(teamId = teamId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched team members!", data = response.body()!!)
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

    override suspend fun updateTeam(
        teamId: Int,
        update: TeamUpdate
    ): Resource<TeamUpdateResponse> {
        val result = try{
            val response = api.updateHousehold(id = teamId, teamUpdate = update)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully updated team!", data = response.body()!!)
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

    override suspend fun deleteTeam(teamId: Int): Resource<TeamDeleteResponse> {
        val result = try{
            val response = api.deleteTeam(id = teamId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully deleted team!", data = response.body()!!)
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
