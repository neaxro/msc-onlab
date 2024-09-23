package com.example.msc_onlab.ui.feature.households

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdsBrief
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.LoggedPersonData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}