package com.example.msc_onlab.data.model.invitation

data class InvitationActiveInvitesItem(
    val created_at: String,
    val email: String,
    val expires: String,
    val id: Int,
    val inviter: Inviter,
    val team: Team,
    val token: String
)