package com.example.msc_onlab.ui.feature.invitation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.invitation.Invitation
import com.example.msc_onlab.data.model.household.invitation.create.CreateInvitationData
import com.example.msc_onlab.data.model.login.LoginData
import com.example.msc_onlab.data.model.login.LoginResponse
import com.example.msc_onlab.data.repository.invitation.InvitationRepository
import com.example.msc_onlab.data.repository.login.LoginRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.helpers.sha256
import com.example.msc_onlab.ui.feature.login.LoginFieldErrors
import com.example.msc_onlab.ui.feature.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    private val invitationRepository: InvitationRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _errors = MutableStateFlow<LoginFieldErrors>(LoginFieldErrors())
    val errors = _errors.asStateFlow()

    private val _invitations = MutableStateFlow<List<Invitation>>(listOf())
    val invitations = _invitations.asStateFlow()

    private fun loadInvitations(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.getInvites(userId = LoggedPersonData.ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    _invitations.value = result.data!!.data
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun acceptInvitation(invitationId: String){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.acceptInvite(invitationId)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun declineInvitation(invitationId: String){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.declineInvite(invitationId)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: InvitationAction){
        when(action){
            InvitationAction.LoadInvitations -> {
                loadInvitations()
            }

            is InvitationAction.AcceptInvite -> {
                acceptInvitation(action.invitationId)
                loadInvitations()
            }

            is InvitationAction.DeclineInvite -> {
                declineInvitation(action.invitationId)
                loadInvitations()
            }
        }
    }
}

sealed class InvitationAction{
    object LoadInvitations : InvitationAction()
    data class AcceptInvite(val invitationId: String) : InvitationAction()
    data class DeclineInvite(val invitationId: String) : InvitationAction()
}
