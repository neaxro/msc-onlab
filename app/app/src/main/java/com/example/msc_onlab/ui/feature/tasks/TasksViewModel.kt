package com.example.msc_onlab.ui.feature.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.household.HouseholdTasksResponse
import com.example.msc_onlab.data.model.household.getPathData
import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.data.repository.task.TaskRepository
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
    private val taskRepository: TaskRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _tasks = MutableStateFlow<List<GetTasksResponseItem>>(listOf())
    val tasks = _tasks.asStateFlow()

    private val _taskActionData = MutableStateFlow<TaskActionData>(TaskActionData())
    val taskActionData = _taskActionData.asStateFlow()

    private fun loadTasks(){
        LoggedPersonData.SELECTED_TEAM_ID ?: return
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = taskRepository.getTasks(LoggedPersonData.SELECTED_TEAM_ID!!)

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

    private fun updateTask(taskId: Int, state: Boolean){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            /*
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

             */
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

            TasksAction.LoadTasks -> {
                loadTasks()
            }
        }
    }
}

sealed class TasksAction{
    data class UpdateTask(val taskId: Int, val state: Boolean): TasksAction()
    object LoadTasks: TasksAction()
}

data class TaskActionData(
    val id: String = "",
    val title: String = "",
)
