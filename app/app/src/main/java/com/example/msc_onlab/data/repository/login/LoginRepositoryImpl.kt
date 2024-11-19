package com.example.msc_onlab.data.repository.login

import android.app.Application
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.remote.LoginApi
import com.example.msc_onlab.domain.wrappers.Resource

class LoginRepositoryImpl(
    private val api: LoginApi,
    private val app: Application
) : LoginRepository {
    private val context = app.applicationContext

    override suspend fun loginPerson(loginData: LoginData): Resource<LoginResponse> {

        val result = try{
            val response = api.login(loginData)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully logged in!", data = response.body()!!)
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
