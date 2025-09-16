package com.example.msc_onlab.ui.feature.tasks.subtask

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.Subtask
import com.example.msc_onlab.data.model.task.v2.subtask.CreateSubtaskData
import com.example.msc_onlab.data.model.task.v2.toUpdateData
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
class SubtasksScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _subtask = MutableStateFlow(com.example.msc_onlab.data.model.task.v2.create.Subtask(false, "title"))
    val subtask = _subtask.asStateFlow()

    private fun deleteSubtask(subtaskId: Int){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = taskRepository.deleteSubtask(subtaskId)

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

    private fun updateSubtask(subtask: Subtask){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            val result = taskRepository.updateSubtask(subtask.toUpdateData())

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

    private fun createSubtask(title: String, taskId: Int){
        _screenState.value = ScreenState.Loading()

        val data = CreateSubtaskData(
            done = false,
            task_id = taskId,
            title = title
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = taskRepository.createSubtask(data)

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

    fun evoke(action: SubtaskActions){
        when(action){
            is SubtaskActions.Delete -> {
                deleteSubtask(action.subtaskId)
            }
            is SubtaskActions.Update -> {
                updateSubtask(action.subtask)
            }

            is SubtaskActions.Create -> {
                createSubtask(action.title, action.taskId)
            }
        }
    }
}

sealed class SubtaskActions{
    data class Create(val title: String, val taskId: Int): SubtaskActions()
    data class Delete(val subtaskId: Int): SubtaskActions()
    data class Update(val subtask: Subtask): SubtaskActions()
}