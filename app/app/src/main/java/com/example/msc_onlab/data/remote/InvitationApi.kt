package com.example.msc_onlab.data.remote

import com.example.msc_onlab.data.model.invitation.FindUserResponse
import com.example.msc_onlab.data.model.invitation.InvitationActiveInvites
import com.example.msc_onlab.data.model.invitation.InvitationCreateData
import com.example.msc_onlab.data.model.invitation.InvitationCreateResponse
import com.example.msc_onlab.data.model.invitation.InvitationRespondResponse
import com.example.msc_onlab.helpers.LoggedPersonData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InvitationApi {
    @Headers("Content-Type: application/json")
    @GET("/invitation/invitations")
    suspend fun getInvitations(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
    ): Response<InvitationActiveInvites>

    @Headers("Content-Type: application/json")
    @GET("/auth/user-search/{username}")
    suspend fun findUserByUsername(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("username") username: String
    ): Response<FindUserResponse>

    @Headers("Content-Type: application/json")
    @GET("/auth/user/{user_id}")
    suspend fun findUserById(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Path("user_id") userId: String
    ): Response<FindUserResponse>

    @Headers("Content-Type: application/json")
    @POST("/invitation/invitations")
    suspend fun createInvitation(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Body invitationData: InvitationCreateData,
    ): Response<InvitationCreateResponse>

    @Headers("Content-Type: application/json")
    @PATCH("/invitation/invitations")
    suspend fun respondInvitation(
        @Header("Authorization") token: String = "Bearer ${LoggedPersonData.TOKEN}",
        @Query("decision") decision: String,
        @Query("token") invitationToken: String
    ): Response<InvitationRespondResponse>
}