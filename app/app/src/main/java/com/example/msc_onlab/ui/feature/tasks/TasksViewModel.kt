package com.example.msc_onlab.ui.feature.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdDetailedResponse
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.ui.feature.households.HouseholdActionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _tasks = MutableStateFlow<HouseholdTasksResponse?>(null)
    val tasks = _tasks.asStateFlow()

    private val _taskActionData = MutableStateFlow<TaskActionData>(TaskActionData())
    val taskActionData = _taskActionData.asStateFlow()

    init {
        if(LoggedPersonData.SELECTED_HOUSEHOLD_ID != null) {
            getTasks()
        }
    }

    private fun getTasks(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.getTasks(householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    _tasks.value = result.data!!
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun deleteTask(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.deleteTask(
                householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!,
                taskId = _taskActionData.value.id
            )

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    val deleteData = result.data!!

                    // Refresh list
                    getTasks()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: TasksAction){
        when(action){
            is TasksAction.ShowSheet -> {
                _taskActionData.update {
                    it.copy(
                        showSheet = true,
                        id = action.id,
                        title = action.title
                    )
                }
            }

            TasksAction.HideSheet -> {
                _taskActionData.update {
                    it.copy(
                        showSheet = false
                    )
                }
            }

            TasksAction.DeleteTask -> {
                _taskActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }

                deleteTask()
            }

            TasksAction.HideDeleteDialog -> {
                _taskActionData.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }
            }
            TasksAction.ShowDeleteDialog -> {
                _taskActionData.update {
                    it.copy(
                        showDeleteDialog = true,
                        showSheet = false
                    )
                }
            }
        }
    }
}

sealed class TasksAction{
    data class ShowSheet(val id: String, val title: String): TasksAction()
    object HideSheet: TasksAction()
    object DeleteTask: TasksAction()
    object ShowDeleteDialog: TasksAction()
    object HideDeleteDialog: TasksAction()
}

data class TaskActionData(
    val showSheet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val id: String = "",
    val title: String = "",
)