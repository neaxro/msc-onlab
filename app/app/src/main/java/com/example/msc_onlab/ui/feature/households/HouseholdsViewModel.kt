package com.example.msc_onlab.ui.feature.households

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _editHouseholdData = MutableStateFlow<EditHouseholdData>(EditHouseholdData())
    val editHouseholdData = _editHouseholdData.asStateFlow()

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

    private fun updateHousehold(){
        val data = _editHouseholdData.value.toUpdateData()
        val id = _editHouseholdData.value.id

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

    fun evoke(action: HouseholdAction){
        when(action){
            is HouseholdAction.SelectHousehold -> {
                selectHousehold(householdID = action.id)
            }

            HouseholdAction.HideEditDialog -> {
                _editHouseholdData.update {
                    it.copy(showDialog = false)
                }
            }
            HouseholdAction.ShowEditDialog -> {
                _editHouseholdData.update {
                    it.copy(showDialog = true)
                }
            }
            is HouseholdAction.SetCurrentNameEditData -> {
                _editHouseholdData.update {
                    it.copy(currentName = action.currentName)
                }
            }
            is HouseholdAction.SetNewNameEditData -> {
                _editHouseholdData.update {
                    it.copy(newName = action.newName, showDialog = false)
                }

                updateHousehold()
            }

            is HouseholdAction.SetIdEditData -> {
                _editHouseholdData.update {
                    it.copy(id = action.id)
                }
            }
        }
    }

    private fun selectHousehold(householdID: String){
        if(LoggedPersonData.SELECTED_HOUSEHOLD_ID == householdID) return

        _screenState.value = ScreenState.Loading()
        LoggedPersonData.SELECTED_HOUSEHOLD_ID = householdID
        _screenState.value = ScreenState.Success(message = "Household selected! ${LoggedPersonData.SELECTED_HOUSEHOLD_ID}", show = true)
    }
}

sealed class HouseholdAction{
    data class SelectHousehold(val id: String) : HouseholdAction()
    object ShowEditDialog : HouseholdAction()
    object HideEditDialog : HouseholdAction()
    data class SetIdEditData(val id: String) : HouseholdAction()
    data class SetCurrentNameEditData(val currentName: String) : HouseholdAction()
    data class SetNewNameEditData(val newName: String) : HouseholdAction()
}

data class EditHouseholdData(
    val showDialog: Boolean = false,
    val id: String = "",
    val currentName: String = "",
    val newName: String = "",
)

fun EditHouseholdData.toUpdateData(): HouseholdUpdateData {
    return HouseholdUpdateData(
        title = this.newName
    )
}