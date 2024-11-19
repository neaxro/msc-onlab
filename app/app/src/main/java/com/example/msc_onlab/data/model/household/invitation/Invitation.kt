package com.example.msc_onlab.data.model.household.invitation

data class Invitation(
    val expiration_date: Double,
    val household_name: String,
    val invitation_id: String,
    val invited_user_id: String,
    val sender: Sender
)