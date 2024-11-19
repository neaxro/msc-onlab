package com.example.msc_onlab.data.repository.register

import com.example.msc_onlab.data.model.register.RegisterData
import com.example.msc_onlab.data.model.register.RegisterResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface RegisterRepository {
    suspend fun register(registerData: RegisterData): Resource<RegisterResponse>
}