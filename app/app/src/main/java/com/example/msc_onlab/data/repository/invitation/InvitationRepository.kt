package com.example.msc_onlab.data.repository.invitation

import com.example.msc_onlab.data.model.invitation.FindUserByUsernameResponse
import com.example.msc_onlab.data.model.invitation.InvitationActiveInvites
import com.example.msc_onlab.data.model.invitation.InvitationCreateData
import com.example.msc_onlab.data.model.invitation.InvitationCreateResponse
import com.example.msc_onlab.data.model.invitation.InvitationRespondResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface InvitationRepository {
    suspend fun getInvites(): Resource<InvitationActiveInvites>

    suspend fun findUser(username: String): Resource<FindUserByUsernameResponse>

    suspend fun createInvite(invitationData: InvitationCreateData): Resource<InvitationCreateResponse>

    suspend fun respondInvitation(decision: Boolean, invitationToken: String): Resource<InvitationRespondResponse>
}