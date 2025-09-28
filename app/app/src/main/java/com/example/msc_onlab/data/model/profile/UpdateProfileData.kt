package com.example.msc_onlab.data.model.profile

data class UpdateProfileData(
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val username: String,
    val profile_picture: String,
)