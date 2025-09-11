package com.example.msc_onlab.data.model.profile

data class GetProfileResponse(
    val disableableCredentialTypes: List<Any>,
    val email: String,
    val emailVerified: Boolean,
    val enabled: Boolean,
    val firstName: String,
    val id: String,
    val lastName: String,
    val notBefore: Int,
    val requiredActions: List<Any>,
    val totp: Boolean,
    val username: String
)