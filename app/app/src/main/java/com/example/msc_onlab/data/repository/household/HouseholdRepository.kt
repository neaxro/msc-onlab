package com.example.msc_onlab.data.repository.household

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
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface HouseholdRepository {
    suspend fun getAllHouseholds(userId: String): Resource<HouseholdsBrief>

    suspend fun updateHousehold(householdId: String, newHouseholdData: HouseholdUpdateData): Resource<HouseholdUpdateResponse>

    suspend fun createHousehold(newHouseholdData: HouseholdCreateData): Resource<HouseholdCreateResponse>

    suspend fun deleteHousehold(householdId: String): Resource<HouseholdDeleteResponse>

    suspend fun getHouseholdById(householdId: String): Resource<HouseholdDetailedResponse>

    suspend fun getUsers(householdId: String): Resource<HouseholdMembersResponse>

    suspend fun getTasks(householdId: String): Resource<HouseholdTasksResponse>

    suspend fun deleteTask(householdId: String, taskId: String): Resource<TaskDeleteResponse>

    suspend fun getMembers(householdId: String): Resource<MembersResponse>
}
