package com.example.msc_onlab.ui.feature.tasks

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.members.MemberData
import com.example.msc_onlab.data.model.task.Data
import com.example.msc_onlab.data.model.task.ResponsibleId
import com.example.msc_onlab.data.model.task.getPathData
import com.example.msc_onlab.data.repository.household.HouseholdRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.LoggedPersonData
import com.example.msc_onlab.helpers.or
import com.example.msc_onlab.helpers.validateTaskDescription
import com.example.msc_onlab.helpers.validateTaskName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val householdRepository: HouseholdRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _task = MutableStateFlow<Data?>(null)
    val task = _task.asStateFlow()

    private val _members = MutableStateFlow<List<MemberData>?>(null)
    val members = _members.asStateFlow()

    private val _errors = MutableStateFlow<EditTaskFieldErrors>(EditTaskFieldErrors())
    val errors = _errors.asStateFlow()

    private var taskId: String

    init {
        taskId = checkNotNull<String>(savedState["taskId"])
        Log.i("EDIT_TASK", "Task id: $taskId")

        getTask(taskId = taskId)
        getMembers()
    }

    private fun getTask(taskId: String){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.getTaskById(taskId = taskId)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()

                    _task.value = result.data!!.data
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun getMembers(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.getMembers(householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    _members.value = result.data!!.data
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun updateTask(showStatus: Boolean = false){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = householdRepository.patchTask(
                householdId = LoggedPersonData.SELECTED_HOUSEHOLD_ID!!,
                taskId = taskId,
                newTaskData = _task.value?.getPathData()!!
            )

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success(message = "Task updated!", show = showStatus)
                    val result = result.data!!
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    fun evoke(action: EditTasksAction){
        when(action){
            is EditTasksAction.UpdateDescription -> {
                _task.update {
                    it?.copy(
                        description = action.description
                    )
                }

                _errors.update {
                    it.copy(
                        description = validateTaskDescription(action.description, applicationContext)
                    )
                }
            }
            is EditTasksAction.UpdateDueDate -> {
                _task.update {
                    it?.copy(
                        due_date = action.dueDate
                    )
                }
            }
            is EditTasksAction.UpdateResponsible -> {
                _task.update {
                    it?.copy(
                        responsible_id = ResponsibleId(action.responsibleId)
                    )
                }
            }
            is EditTasksAction.UpdateTitle -> {
                _task.update {
                    it?.copy(
                        title = action.name
                    )
                }

                _errors.update {
                    it.copy(
                        name = validateTaskName(action.name, applicationContext)
                    )
                }
            }

            is EditTasksAction.ChangeSubtaskStatus -> {

                val updatedSubtasks = _task.value?.subtasks?.map { subtask ->
                    if (subtask._id.`$oid` == action.subtaskId) {
                        subtask.copy(done = action.status)
                    } else {
                        subtask
                    }
                }

                updatedSubtasks ?: return

                _task.update {
                    it?.copy(
                        subtasks = updatedSubtasks
                    )
                }

                updateTask()
            }

            is EditTasksAction.DeleteSubtask -> {
                val updatedSubtasks = _task.value?.subtasks?.filter { subtask ->
                    subtask._id.`$oid` != action.subtaskId
                }

                updatedSubtasks ?: return

                _task.update {
                    it?.copy(
                        subtasks = updatedSubtasks
                    )
                }
            }

            EditTasksAction.SaveTask -> {
                updateTask(showStatus = true)
            }
        }
    }
}

sealed class EditTasksAction{
    data class UpdateTitle(val name: String) : EditTasksAction()
    data class UpdateDueDate(val dueDate: String) : EditTasksAction()
    data class UpdateResponsible(val responsibleId: String) : EditTasksAction()
    data class UpdateDescription(val description: String) : EditTasksAction()
    data class ChangeSubtaskStatus(val subtaskId: String, val status: Boolean) : EditTasksAction()
    data class DeleteSubtask(val subtaskId: String) : EditTasksAction()
    object SaveTask : EditTasksAction()
}

data class EditTaskFieldErrors(
    val name: DataFieldErrors = DataFieldErrors.NoError,
    val description: DataFieldErrors = DataFieldErrors.NoError
)

fun EditTaskFieldErrors.anyErrors(): Boolean {
    val error = this.name
        .or(this.description)

    return error !is DataFieldErrors.NoError
}
