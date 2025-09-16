package com.example.msc_onlab.ui.feature.households

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.team.TeamUpdate
import com.example.msc_onlab.data.model.team.TeamsBrief
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.data.repository.team.TeamRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.LoggedPersonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val teamRepository: TeamRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _teams = MutableStateFlow<TeamsBrief?>(null)
    val teams = _teams.asStateFlow()

    private val _teamActionData = MutableStateFlow<TeamActionData>(TeamActionData())
    val teamActionData = _teamActionData.asStateFlow()

    private fun loadTeams(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = teamRepository.getAllTeams()

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    _teams.value = result.data!!
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun updateTeam(newTitle: String, newDescription: String){
        val data = TeamUpdate(
            name = newTitle,
            description = newDescription
        )
        val id = _teamActionData.value.id

        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {

            var result = teamRepository.updateTeam(id, data)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    val resultData = result.data!!

                    // Refresh list
                    loadTeams()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun createHousehold(title: String){
        val householdData = HouseholdCreateData(title = title)
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.createHousehold(newHouseholdData = householdData)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(message = "Household created!", show = true)
                    val resultData = result.data!!

                    // Refresh list
                    loadTeams()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun deleteHousehold(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = teamRepository.deleteTeam(_teamActionData.value.id)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(message = "Team deleted!", show = true)
                    val resultData = result.data!!

                    // Refresh list
                    loadTeams()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: TeamAction){
        when(action){
            is TeamAction.SelectTeam -> {
                selectTeam(teamId = action.id)
            }

            TeamAction.HideEditDialog -> {
                _teamActionData.update {
                    it.copy(showEditDialog = false)
                }
            }
            TeamAction.ShowEditDialog -> {
                _teamActionData.update {
                    it.copy(
                        showEditDialog = true,
                        showSheet = false
                    )
                }
            }

            is TeamAction.CreateHousehold -> {
                _teamActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
                createHousehold(title = action.title)
            }

            is TeamAction.ShowSheet -> {
                _teamActionData.update {
                    it.copy(
                        showSheet = true,
                        title = action.title,
                        id = action.id
                    )
                }
            }
            TeamAction.HideSheet -> {
                _teamActionData.update {
                    it.copy(
                        showSheet = false
                    )
                }
            }

            is TeamAction.EditTeam -> {
                _teamActionData.update {
                    it.copy(
                        showEditDialog = false
                    )
                }
                updateTeam(newTitle = action.newTitle, newDescription = action.newDescription)
            }

            TeamAction.HideCreateDialog -> {
                _teamActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
            }
            TeamAction.ShowCreateDialog -> {
                _teamActionData.update {
                    it.copy(
                        showCreateDialog = true
                    )
                }
            }

            TeamAction.ShowDeleteDialog -> {
                _teamActionData.update {
                    it.copy(
                        showDeleteDialog = true,
                        showSheet = false
                    )
                }
            }

            TeamAction.HideDeleteDialog -> {
                _teamActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
            }
            TeamAction.DeleteTeam -> {
                _teamActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }

                deleteHousehold()
            }

            TeamAction.LoadTeams -> {
                loadTeams()
            }
        }
    }

    private fun selectTeam(teamId: Int){
        if(LoggedPersonData.SELECTED_TEAM_ID == teamId) return

        _screenState.value = ScreenState.Loading()
        LoggedPersonData.SELECTED_TEAM_ID = teamId
        _teamActionData.update { it.copy(selectedId = teamId) }
        _screenState.value = ScreenState.Success(message = "Team selected!", show = false)
    }
}

sealed class TeamAction{
    object LoadTeams : TeamAction()
    data class SelectTeam(val id: Int) : TeamAction()
    data class ShowSheet(val id: Int, val title: String) : TeamAction()
    object HideSheet : TeamAction()
    object ShowEditDialog : TeamAction()
    object HideEditDialog : TeamAction()
    data class EditTeam(val newTitle: String, val newDescription: String) : TeamAction()
    object ShowCreateDialog : TeamAction()
    object HideCreateDialog : TeamAction()
    data class CreateHousehold(val title: String) : TeamAction()
    object ShowDeleteDialog : TeamAction()
    object HideDeleteDialog : TeamAction()
    object DeleteTeam : TeamAction()
}

data class TeamActionData(
    val showSheet: Boolean = false,
    val showEditDialog: Boolean = false,
    val showInviteDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showCreateDialog: Boolean = false,
    val id: Int = 0,
    val selectedId: Int = LoggedPersonData.SELECTED_TEAM_ID ?: -1,
    val title: String = "",
    val description: String = "",
)
