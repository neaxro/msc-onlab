package com.example.msc_onlab.data.repository.invitation

import android.app.Application
import com.example.msc_onlab.data.model.invitation.FindUserByUsernameResponse
import com.example.msc_onlab.data.model.invitation.InvitationActiveInvites
import com.example.msc_onlab.data.model.invitation.InvitationCreateData
import com.example.msc_onlab.data.model.invitation.InvitationCreateResponse
import com.example.msc_onlab.data.model.invitation.InvitationRespondResponse
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.InvitationApi
import com.example.msc_onlab.domain.wrappers.Resource

class InvitationRepositoryImpl(
    private val api: InvitationApi,
    private val app: Application
) : InvitationRepository {
    override suspend fun getInvites(): Resource<InvitationActiveInvites> {
        val result = try{
            val response = api.getInvitations()

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully fetched all invitations!", data = response.body()!!)
            }
            else{
                // Server error
                Resource.Error(message = response.errorBody()!!.string())
            }

            res
        } catch (e: Exception){
            // Network error
            Resource.Error("Network error occurred.")
        }

        return result
    }

    override suspend fun findUser(username: String): Resource<FindUserByUsernameResponse> {
        val result = try{
            val response = api.findUserByUsername(username = username)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully find user!", data = response.body()!!)
            }
            else{
                // Server error
                Resource.Error(message = response.errorBody()!!.string())
            }

            res
        } catch (e: Exception){
            // Network error
            Resource.Error("Network error occurred.")
        }

        return result
    }

    override suspend fun createInvite(invitationData: InvitationCreateData): Resource<InvitationCreateResponse> {
        val result = try{
            val response = api.createInvitation(invitationData = invitationData)

            // Check server response
            val res = if(response.code() == 201){
                Resource.Success(message = "Successfully created an invitation!", data = response.body()!!)
            }
            else{
                // Server error
                Resource.Error(message = response.errorBody()!!.string())
            }

            res
        } catch (e: Exception){
            // Network error
            Resource.Error("Network error occurred.")
        }

        return result
    }

    override suspend fun respondInvitation(
        decision: Boolean,
        invitationToken: String
    ): Resource<InvitationRespondResponse> {
        val result = try{
            val response = api.respondInvitation(
                decision = if (decision) "accept" else "decline",
                invitationToken = invitationToken)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully accepted invitation!", data = response.body()!!)
            }
            else{
                // Server error
                Resource.Error(message = response.errorBody()!!.string())
            }

            res
        } catch (e: Exception){
            // Network error
            Resource.Error("Network error occurred.")
        }

        return result
    }
}