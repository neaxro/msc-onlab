package com.example.msc_onlab.data.repository.profile

import android.app.Application
import com.example.msc_onlab.data.model.profile.GetProfileResponse
import com.example.msc_onlab.data.model.profile.ProfileResponse
import com.example.msc_onlab.data.model.profile.UpdateProfileData
import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.data.remote.ProfileApi
import com.example.msc_onlab.domain.wrappers.Resource

class ProfileRepositoryImpl (
    private val api: ProfileApi,
    private val app: Application
) : ProfileRepository {
    override suspend fun getProfile(userId: String): Resource<GetProfileResponse> {
        val result = try{
            val response = api.getProfile(userId = userId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched profile!", data = response.body()!!)
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

    override suspend fun updateProfile(updateData: UpdateProfileData): Resource<ProfileResponse> {
        val result = try{
            val response = api.patchProfile(updateProfileData = updateData)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully updated profile!", data = response.body()!!)
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
