package com.example.msc_onlab.ui.feature.members

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdCreateData
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.model.members.MembersResponse
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
class MembersViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _members = MutableStateFlow<MembersResponse?>(null)
    val members = _members.asStateFlow()

    init {
        if(LoggedPersonData.SELECTED_HOUSEHOLD_ID != null){
            getMembers()
        }
    }

    private fun getMembers(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.getMembers(householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    _members.value = result.data!!
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }
}
