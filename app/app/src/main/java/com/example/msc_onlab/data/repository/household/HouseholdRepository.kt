package com.example.msc_onlab.data.repository.household

import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdCreateResponse
import com.example.msc_onlab.data.model.household.HouseholdDeleteResponse
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateResponse
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.domain.wrappers.Resource
import retrofit2.Response

interface HouseholdRepository {
    suspend fun getAllHouseholds(userId: String): Resource<HouseholdsBrief>

    suspend fun updateHousehold(householdId: String, newHouseholdData: HouseholdUpdateData): Resource<HouseholdUpdateResponse>

    suspend fun createHousehold(newHouseholdData: HouseholdCreateData): Resource<HouseholdCreateResponse>

    suspend fun deleteHousehold(householdId: String): Resource<HouseholdDeleteResponse>
}