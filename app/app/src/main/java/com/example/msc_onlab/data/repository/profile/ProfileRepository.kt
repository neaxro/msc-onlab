package com.example.msc_onlab.data.repository.profile

import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.model.profile.GetProfileResponse
import com.example.msc_onlab.data.model.profile.ProfileResponse
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.domain.wrappers.Resource

interface ProfileRepository {
    suspend fun getProfile(userId: String): Resource<GetProfileResponse>

    suspend fun updateProfile(updateData: UpdateProfileData): Resource<ProfileResponse>
}
