package com.example.msc_onlab.data.model.profile

data class GetProfileResponse(
    val `data`: ProfileData,
    val status: String,
    val time: String
)