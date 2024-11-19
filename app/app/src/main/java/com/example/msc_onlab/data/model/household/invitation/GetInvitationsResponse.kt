package com.example.msc_onlab.data.model.household.invitation

data class GetInvitationsResponse(
    val `data`: List<Invitation>,
    val no_data: Int,
    val status: String,
    val time: String
)