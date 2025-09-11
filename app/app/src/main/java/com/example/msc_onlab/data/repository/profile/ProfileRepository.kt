package com.example.msc_onlab.data.repository.profile

import com.example.msc_onlab.data.model.profile.GetProfileResponse
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.data.model.profile.UpdateProfileResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface ProfileRepository {
    suspend fun getProfile(userId: String): Resource<GetProfileResponse>

    suspend fun updateProfile(userId: String, updateData: UpdateProfileData): Resource<UpdateProfileResponse>
}
