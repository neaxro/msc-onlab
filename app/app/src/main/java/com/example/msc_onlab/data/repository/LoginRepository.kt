package com.example.msc_onlab.data.repository

import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface LoginRepository {
    suspend fun loginPerson(loginData: LoginData): Resource<LoginResponse>
}