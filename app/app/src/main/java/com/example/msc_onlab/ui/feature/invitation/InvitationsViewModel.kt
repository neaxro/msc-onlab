package com.example.msc_onlab.ui.feature.invitation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.invitation.InvitationActiveInvitesItem
import com.example.msc_onlab.data.repository.invitation.InvitationRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.ui.feature.login.LoginFieldErrors
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

    private val _invitations = MutableStateFlow<List<InvitationActiveInvitesItem>>(listOf())
    val invitations = _invitations.asStateFlow()

    private fun loadInvitations(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.getInvites()

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    _invitations.value = result.data!!
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun acceptInvitation(invitationToken: String){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.respondInvitation(true,invitationToken)

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

    private fun declineInvitation(invitationToken: String){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = invitationRepository.respondInvitation(false, invitationToken)

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
                acceptInvitation(action.invitationToken)
                loadInvitations()
            }

            is InvitationAction.DeclineInvite -> {
                declineInvitation(action.invitationToken)
                loadInvitations()
            }
        }
    }
}

sealed class InvitationAction{
    object LoadInvitations : InvitationAction()
    data class AcceptInvite(val invitationToken: String) : InvitationAction()
    data class DeclineInvite(val invitationToken: String) : InvitationAction()
}
