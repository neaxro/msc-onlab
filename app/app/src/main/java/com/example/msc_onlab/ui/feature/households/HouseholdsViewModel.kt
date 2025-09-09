package com.example.msc_onlab.ui.feature.households

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
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
class HouseholdsViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val teamRepository: TeamRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    //private val _households = MutableStateFlow<HouseholdsBrief?>(null)
    //val households = _households.asStateFlow()

    private val _teams = MutableStateFlow<TeamsBrief?>(null)
    val teams = _teams.asStateFlow()

    private val _householdActionData = MutableStateFlow<HouseholdActionData>(HouseholdActionData())
    val householdActionData = _householdActionData.asStateFlow()

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

    private fun updateHousehold(newTitle: String){
        val data = HouseholdUpdateData(title = newTitle)
        val id = _householdActionData.value.id

        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.updateHousehold(householdId = id, newHouseholdData = data)

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
            var result = householdRepository.deleteHousehold(householdId = _householdActionData.value.id)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(message = "Household deleted!", show = true)
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
            is TeamAction.SelectHousehold -> {
                selectHousehold(householdID = action.id)
            }

            TeamAction.HideEditDialog -> {
                _householdActionData.update {
                    it.copy(showEditDialog = false)
                }
            }
            TeamAction.ShowEditDialog -> {
                _householdActionData.update {
                    it.copy(
                        showEditDialog = true,
                        showSheet = false
                    )
                }
            }

            is TeamAction.CreateHousehold -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
                createHousehold(title = action.title)
            }

            is TeamAction.ShowSheet -> {
                _householdActionData.update {
                    it.copy(
                        showSheet = true,
                        title = action.title,
                        id = action.id
                    )
                }
            }
            TeamAction.HideSheet -> {
                _householdActionData.update {
                    it.copy(
                        showSheet = false
                    )
                }
            }

            is TeamAction.EditHousehold -> {
                _householdActionData.update {
                    it.copy(
                        showEditDialog = false
                    )
                }
                updateHousehold(newTitle = action.newTitle)
            }

            TeamAction.HideCreateDialog -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
            }
            TeamAction.ShowCreateDialog -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = true
                    )
                }
            }

            TeamAction.ShowDeleteDialog -> {
                _householdActionData.update {
                    it.copy(
                        showDeleteDialog = true,
                        showSheet = false
                    )
                }
            }

            TeamAction.HideDeleteDialog -> {
                _householdActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
            }
            TeamAction.DeleteHousehold -> {
                _householdActionData.update {
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

    private fun selectHousehold(householdID: String){
        if(LoggedPersonData.SELECTED_HOUSEHOLD_ID == householdID) return

        _screenState.value = ScreenState.Loading()
        LoggedPersonData.SELECTED_HOUSEHOLD_ID = householdID
        _screenState.value = ScreenState.Success(message = "Household selected!", show = false)
    }
}

sealed class TeamAction{
    object LoadTeams : TeamAction()
    data class SelectHousehold(val id: String) : TeamAction()
    data class ShowSheet(val id: String, val title: String) : TeamAction()
    object HideSheet : TeamAction()
    object ShowEditDialog : TeamAction()
    object HideEditDialog : TeamAction()
    data class EditHousehold(val newTitle: String) : TeamAction()
    object ShowCreateDialog : TeamAction()
    object HideCreateDialog : TeamAction()
    data class CreateHousehold(val title: String) : TeamAction()
    object ShowDeleteDialog : TeamAction()
    object HideDeleteDialog : TeamAction()
    object DeleteHousehold : TeamAction()
}

data class HouseholdActionData(
    val showSheet: Boolean = false,
    val showEditDialog: Boolean = false,
    val showInviteDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showCreateDialog: Boolean = false,
    val id: String = "",
    val title: String = "",
)
