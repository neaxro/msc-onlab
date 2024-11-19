package com.example.msc_onlab.data.repository.invitation

import com.example.msc_onlab.data.model.household.invitation.GetInvitationsResponse
import com.example.msc_onlab.data.model.household.invitation.RespondeInvitationResponse
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationData
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface InvitationRepository {
    suspend fun getInvites(userId: String): Resource<GetInvitationsResponse>

    suspend fun createInvite(invitationData: CreateInvitationData): Resource<CreateInvitationResponse>

    suspend fun acceptInvite(invitationId: String): Resource<RespondeInvitationResponse>

    suspend fun declineInvite(invitationId: String): Resource<RespondeInvitationResponse>
}