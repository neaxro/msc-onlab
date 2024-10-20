package com.example.msc_onlab.data.model.profile

data class UpdateProfileData(
    val _id: Id,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val profile_picture: String,
    val username: String
)