package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationData
import com.example.msc_onlab.data.model.household.invitation.GetInvitationsResponse
import com.example.msc_onlab.data.model.household.invitation.RespondeInvitationResponse
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface InvitationApi {
    @Headers("Content-Type: application/json")
    @GET("/household/invite/{user_id}")
    suspend fun getInvitations(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String,
    ): Response<GetInvitationsResponse>

    @Headers("Content-Type: application/json")
    @POST("/household/invite")
    suspend fun createInvitation(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body invitationData: CreateInvitationData,
    ): Response<CreateInvitationResponse>

    @Headers("Content-Type: application/json")
    @POST("/household/accept-invite/{invitation_id}")
    suspend fun acceptInvitation(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("invitation_id") invitationId: String,
    ): Response<RespondeInvitationResponse>

    @Headers("Content-Type: application/json")
    @POST("/household/decline-invite/{invitation_id}")
    suspend fun declineInvitation(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("invitation_id") invitationId: String,
    ): Response<RespondeInvitationResponse>
}