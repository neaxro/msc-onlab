package com.example.msc_onlab.data.repository.household

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
}