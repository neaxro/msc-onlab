package com.example.msc_onlab.data.repository.team

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
import com.example.msc_onlab.data.model.team.CreateTeamData
import com.example.msc_onlab.data.model.team.CreateTeamResponse
import com.example.msc_onlab.data.model.team.TeamDeleteResponse
import com.example.msc_onlab.data.model.team.TeamInfo
import com.example.msc_onlab.data.model.team.TeamMembers
import com.example.msc_onlab.data.model.team.TeamUpdate
import com.example.msc_onlab.data.model.team.TeamUpdateResponse
import com.example.msc_onlab.data.model.team.TeamsBrief
import com.example.msc_onlab.domain.wrappers.Resource

interface TeamRepository {
    suspend fun getAllTeams(): Resource<TeamsBrief>

    suspend fun getTeamInfo(teamId: Int): Resource<TeamInfo>

    suspend fun getTeamMembers(teamId: Int): Resource<TeamMembers>

    suspend fun createTeam(data: CreateTeamData): Resource<CreateTeamResponse>

    suspend fun updateTeam(teamId: Int, update: TeamUpdate): Resource<TeamUpdateResponse>

    suspend fun deleteTeam(teamId: Int): Resource<TeamDeleteResponse>
}
