package com.example.msc_onlab.data.repository.register

import android.app.Application
import com.example.msc_onlab.data.model.register.RegisterData
import com.example.msc_onlab.data.model.register.RegisterResponse
import com.example.msc_onlab.data.remote.RegisterApi
import com.example.msc_onlab.domain.wrappers.Resource

class RegisterRepositoryImpl(
    private val api: RegisterApi,
    private val app: Application
) : RegisterRepository {
    private val context = app.applicationContext

    override suspend fun register(registerData: RegisterData): Resource<RegisterResponse> {

        val result = try{
            val response = api.register(registerData)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully registered!", data = response.body()!!)
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