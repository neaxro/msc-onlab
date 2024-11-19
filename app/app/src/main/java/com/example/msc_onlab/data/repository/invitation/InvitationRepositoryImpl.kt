package com.example.msc_onlab.data.repository.invitation

import android.app.Application
import com.example.msc_onlab.data.model.household.invitation.GetInvitationsResponse
import com.example.msc_onlab.data.model.household.invitation.RespondeInvitationResponse
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationData
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationResponse
import com.example.msc_onlab.data.remote.HouseholdApi
import com.example.msc_onlab.data.remote.InvitationApi
import com.example.msc_onlab.domain.wrappers.Resource

class InvitationRepositoryImpl(
    private val api: InvitationApi,
    private val app: Application
) : InvitationRepository {
    override suspend fun getInvites(userId: String): Resource<GetInvitationsResponse> {
        val result = try{
            val response = api.getInvitations(userId = userId)

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

    override suspend fun createInvite(invitationData: CreateInvitationData): Resource<CreateInvitationResponse> {
        val result = try{
            val response = api.createInvitation(invitationData = invitationData)

            // Check server response
            val res = if(response.code() == 200){
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

    override suspend fun acceptInvite(invitationId: String): Resource<RespondeInvitationResponse> {
        val result = try{
            val response = api.acceptInvitation(invitationId = invitationId)

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

    override suspend fun declineInvite(invitationId: String): Resource<RespondeInvitationResponse> {
        val result = try{
            val response = api.declineInvitation(invitationId = invitationId)

            // Check server response
            val res = if(response.code() == 200){
                Resource.Success(message = "Successfully declined invitation!", data = response.body()!!)
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