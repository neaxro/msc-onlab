package com.example.msc_onlab.data.model.household.invitation.create

data class CreateInvitationData(
    val household_id: String,
    val invited_user_name: String,
    val sender_id: String
)