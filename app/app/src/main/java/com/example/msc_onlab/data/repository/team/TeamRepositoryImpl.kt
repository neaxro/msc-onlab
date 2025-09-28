package com.example.msc_onlab.data.repository.team

import android.app.Application
import com.example.msc_onlab.data.model.task.create.CreateTaskData
import com.example.msc_onlab.data.model.team.CreateTeamData
import com.example.msc_onlab.data.model.team.CreateTeamResponse
import com.example.msc_onlab.data.model.team.TeamDeleteResponse
import com.example.msc_onlab.data.model.team.TeamInfo
import com.example.msc_onlab.data.model.team.TeamMembers
import com.example.msc_onlab.data.model.team.TeamUpdate
import com.example.msc_onlab.data.model.team.TeamUpdateResponse
import com.example.msc_onlab.data.model.team.TeamsBrief
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

    override suspend fun createTeam(data: CreateTeamData): Resource<CreateTeamResponse> {
        val result = try{
            val response = api.createTeam(data = data)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully created team!", data = response.body()!!)
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
            val response = api.updateTeam(id = teamId, teamUpdate = update)

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
