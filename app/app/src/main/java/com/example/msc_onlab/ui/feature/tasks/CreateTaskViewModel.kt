package com.example.msc_onlab.ui.feature.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskData
import com.example.msc_onlab.data.model.task.v2.create.Subtask
import com.example.msc_onlab.data.model.team.TeamMembersItem
import com.example.msc_onlab.data.repository.task.TaskRepository
import com.example.msc_onlab.data.repository.team.TeamRepository
import com.example.msc_onlab.domain.wrappers.Resource
import com.example.msc_onlab.domain.wrappers.ScreenState
import com.example.msc_onlab.helpers.DataFieldErrors
import com.example.msc_onlab.helpers.DataFieldErrors.*
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
class CreateTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val teamRepository: TeamRepository,
    private val applicationContext: Context
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _task = MutableStateFlow<CreateTaskData>(CreateTaskData())
    val task = _task.asStateFlow()

    private val _members = MutableStateFlow<List<TeamMembersItem>>(listOf())
    val members = _members.asStateFlow()

    private val _errors = MutableStateFlow<TaskFieldErrors>(TaskFieldErrors())
    val errors = _errors.asStateFlow()

    init {
        if(LoggedPersonData.SELECTED_TEAM_ID != null){
            getMembers()
        }
    }

    private fun getMembers(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            var result = teamRepository.getTeamMembers(LoggedPersonData.SELECTED_TEAM_ID!!)

            when(result){
                is Resource.Success -> {
                    _screenState.value = ScreenState.Success()
                    _members.value = result.data!!

                    // Assign the first member to the task by default
                    _task.update { it.copy(responsible_id = _members.value.first().id) }
                }
                is Resource.Error -> {
                    _screenState.value = ScreenState.Error(message = result.message!!)
                }
            }
        }
    }

    private fun createTask(){
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch(Dispatchers.IO) {

            val result = taskRepository.createTask(
                taskData = _task.value.copy(
                    team_id = LoggedPersonData.SELECTED_TEAM_ID!!
                )
            )

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

    fun evoke(action: CreateTasksAction){
        when(action){
            is CreateTasksAction.UpdateTitle -> {
                _task.update {
                    it.copy(
                        title = action.name
                    )
                }

                _errors.update {
                    it.copy(
                        name = validateTaskName(action.name, applicationContext)
                    )
                }
            }
            is CreateTasksAction.UpdateDescription -> {
                _task.update {
                    it.copy(
                        description = action.description
                    )
                }

                _errors.update {
                    it.copy(
                        description = validateTaskDescription(action.description, applicationContext)
                    )
                }
            }
            is CreateTasksAction.UpdateDueDate -> {
                _task.update {
                    it.copy(
                        due_date = action.dueDate
                    )
                }

                _errors.update {
                    it.copy(
                        dueDate = if (action.dueDate.isEmpty()) {
                            TaskError("Cannot be empty!")
                        }
                        else{
                            DataFieldErrors.NoError
                        }
                    )
                }
            }
            is CreateTasksAction.UpdateResponsible -> {
                _task.update {
                    it.copy(
                        responsible_id = action.responsibleId
                    )
                }
            }


            is CreateTasksAction.AddSubtask -> {
                val newSubtask = Subtask(title = action.name, done = false)
                val subtasks = listOf(*_task.value.subtasks.toTypedArray(), newSubtask)

                _task.update {
                    it.copy(
                        subtasks = subtasks
                    )
                }
            }
            is CreateTasksAction.DeleteSubtask -> {
                val subtasks = _task.value.subtasks.filterIndexed { index, subtask ->
                    index != action.number
                }

                _task.update {
                    it.copy(
                        subtasks = subtasks
                    )
                }
            }
            CreateTasksAction.CreateTask -> {
                createTask()
            }

            is CreateTasksAction.UpdateSubtask -> {
                val newSubtasks = _task.value.subtasks.toMutableList()
                newSubtasks[action.number] = newSubtasks[action.number].copy(done = action.isDone)

                _task.update {
                    it.copy(subtasks = newSubtasks)
                }
            }
        }
    }
}

sealed class CreateTasksAction {
    data class UpdateTitle(val name: String) : CreateTasksAction()
    data class UpdateDueDate(val dueDate: String) : CreateTasksAction()
    data class UpdateResponsible(val responsibleId: String) : CreateTasksAction()
    data class UpdateDescription(val description: String) : CreateTasksAction()
    data class AddSubtask(val name: String) : CreateTasksAction()
    data class DeleteSubtask(val number: Int) : CreateTasksAction()
    data class UpdateSubtask(val number: Int, val isDone: Boolean) : CreateTasksAction()
    object CreateTask : CreateTasksAction()
}

data class TaskFieldErrors(
    val name: DataFieldErrors = DataFieldErrors.TaskError(""),
    val description: DataFieldErrors = DataFieldErrors.NoError,
    val dueDate: DataFieldErrors = DataFieldErrors.TaskError("Cannot be empty!"),
    val subtasks: List<SubtaskFieldErrors> = listOf(),
)

data class SubtaskFieldErrors(
    val name: String,
    val nameError: DataFieldErrors = DataFieldErrors.NoError,
)

fun TaskFieldErrors.anyErrors(): Boolean {
    val error = this.name
        .or(this.description)
        .or(this.dueDate)
        .or(
            this.subtasks.firstOrNull {
                it.nameError != DataFieldErrors.NoError
            }?.nameError ?: DataFieldErrors.NoError
        )

    return error !is DataFieldErrors.NoError
}