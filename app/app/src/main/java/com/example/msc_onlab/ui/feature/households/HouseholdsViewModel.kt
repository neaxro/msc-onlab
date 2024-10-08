package com.example.msc_onlab.ui.feature.households

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdUpdateData
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.repository.household.HouseholdRepository
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
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _households = MutableStateFlow<HouseholdsBrief?>(null)
    val households = _households.asStateFlow()

    private val _householdActionData = MutableStateFlow<HouseholdActionData>(HouseholdActionData())
    val householdActionData = _householdActionData.asStateFlow()

    init {
        loadHouseholds()
    }

    private fun loadHouseholds(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.getAllHouseholds(LoggedPersonData.ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    _households.value = result.data!!
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
                    loadHouseholds()
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
                    loadHouseholds()
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
                    loadHouseholds()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: HouseholdAction){
        when(action){
            is HouseholdAction.SelectHousehold -> {
                selectHousehold(householdID = action.id)
            }

            HouseholdAction.HideEditDialog -> {
                _householdActionData.update {
                    it.copy(showEditDialog = false)
                }
            }
            HouseholdAction.ShowEditDialog -> {
                _householdActionData.update {
                    it.copy(
                        showEditDialog = true,
                        showSheet = false
                    )
                }
            }

            is HouseholdAction.CreateHousehold -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
                createHousehold(title = action.title)
            }

            is HouseholdAction.ShowSheet -> {
                _householdActionData.update {
                    it.copy(
                        showSheet = true,
                        title = action.title,
                        id = action.id
                    )
                }
            }
            HouseholdAction.HideSheet -> {
                _householdActionData.update {
                    it.copy(
                        showSheet = false
                    )
                }
            }

            is HouseholdAction.EditHousehold -> {
                _householdActionData.update {
                    it.copy(
                        showEditDialog = false
                    )
                }
                updateHousehold(newTitle = action.newTitle)
            }

            HouseholdAction.HideCreateDialog -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = false
                    )
                }
            }
            HouseholdAction.ShowCreateDialog -> {
                _householdActionData.update {
                    it.copy(
                        showCreateDialog = true
                    )
                }
            }

            HouseholdAction.ShowDeleteDialog -> {
                _householdActionData.update {
                    it.copy(
                        showDeleteDialog = true,
                        showSheet = false
                    )
                }
            }

            HouseholdAction.HideDeleteDialog -> {
                _householdActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
            }
            HouseholdAction.DeleteHousehold -> {
                _householdActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }

                deleteHousehold()
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

sealed class HouseholdAction{
    data class SelectHousehold(val id: String) : HouseholdAction()
    data class ShowSheet(val id: String, val title: String) : HouseholdAction()
    object HideSheet : HouseholdAction()
    object ShowEditDialog : HouseholdAction()
    object HideEditDialog : HouseholdAction()
    data class EditHousehold(val newTitle: String) : HouseholdAction()
    object ShowCreateDialog : HouseholdAction()
    object HideCreateDialog : HouseholdAction()
    data class CreateHousehold(val title: String) : HouseholdAction()
    object ShowDeleteDialog : HouseholdAction()
    object HideDeleteDialog : HouseholdAction()
    object DeleteHousehold : HouseholdAction()
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
