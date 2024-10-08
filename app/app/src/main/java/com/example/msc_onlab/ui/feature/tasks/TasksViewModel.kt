package com.example.msc_onlab.ui.feature.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.model.household.getPathData
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
            loadTasks()
        }
    }

    private fun loadTasks(){
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



    private fun updateTask(taskId: String, state: Boolean){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.patchTask(
                householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!,
                taskId = taskId,
                newTaskData = _tasks.value!!.data
                    .first {
                        it._id.`$oid` == taskId
                    }.copy(
                        done = state
                    ).getPathData()
            )

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    val result = result.data!!

                    // Refresh list
                    loadTasks()
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: TasksAction){
        when(action){
            is TasksAction.UpdateTask -> {
                updateTask(
                    taskId = action.taskId,
                    state = action.state
                )
            }
        }
    }
}

sealed class TasksAction{
    data class UpdateTask(val taskId: String, val state: Boolean): TasksAction()
}

data class TaskActionData(
    val id: String = "",
    val title: String = "",
)
