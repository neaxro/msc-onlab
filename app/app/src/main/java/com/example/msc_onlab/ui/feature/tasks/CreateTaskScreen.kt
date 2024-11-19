package com.example.msc_onlab.ui.feature.tasks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.helpers.Constants
import com.example.msc_onlab.helpers.isError
import com.example.msc_onlab.ui.feature.common.AddNewSubtaskItem
import com.example.msc_onlab.ui.feature.common.DatePickerDocked
import com.example.msc_onlab.ui.feature.common.DeleteDialog
import com.example.msc_onlab.ui.feature.common.MemberDropDownMenu
import com.example.msc_onlab.ui.feature.common.MySnackBarHost
import com.example.msc_onlab.ui.feature.common.MyTopAppBar
import com.example.msc_onlab.ui.feature.common.SmartOutlinedTextField
import com.example.msc_onlab.ui.feature.common.SubtaskBriefListItem
import com.example.msc_onlab.ui.feature.common.SubtaskCreateListItem
import com.example.msc_onlab.ui.theme.Shapes

@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    val controller = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val task = viewModel.task.collectAsState().value
    val members = viewModel.members.collectAsState().value
    val errors = viewModel.errors.collectAsState().value

    var selectedTabIndex by rememberSaveable { mutableStateOf<TaskEditPage>(TaskEditPage.EditPage) }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = if(task.title.isNotEmpty()) task.title else "Create task",
                screenState = viewModel.screenState.collectAsState(),
            )
        },
        snackbarHost = {
            MySnackBarHost(screenState = viewModel.screenState)
        },
        modifier = modifier.fillMaxSize(),
    ) { padding ->
        if(members != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TabRow(selectedTabIndex = selectedTabIndex.ordinal) {
                    TaskEditPage.entries.forEachIndexed() { index, tabPage ->
                        Tab(
                            selected = index == selectedTabIndex.ordinal,
                            onClick = { selectedTabIndex = tabPage },
                            text = { Text(text = tabPage.title) },
                        )
                    }
                }

                when(selectedTabIndex){
                    TaskEditPage.EditPage -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Spacer(modifier = Modifier.padding(vertical = 15.dp))

                            SmartOutlinedTextField(
                                value = task.title,
                                label = { Text(text = "Title") },
                                onValueChange = { newTitle ->
                                    viewModel.evoke(CreateTasksAction.UpdateTitle(newTitle))
                                },
                                isError = errors.name.isError(),
                                errorMessage = errors.name.message,
                                singleLine = true,
                                maxLength = Constants.MAX_TASK_TITLE_LENGTH,
                                readOnly = readOnly,
                                enabled = true
                            )

                            DatePickerDocked(
                                value = task.due_date,
                                label = { Text(text = "Due date") },
                                onValueChange = { newDueDate ->
                                    viewModel.evoke(CreateTasksAction.UpdateDueDate(newDueDate))
                                },
                                readOnly = readOnly,
                                isError = errors.dueDate.isError(),
                            )

                            Spacer(modifier = Modifier.padding(vertical = 10.dp))

                            MemberDropDownMenu(
                                members = members,
                                selected = members.firstOrNull { it._id.`$oid` == task.responsible_id }
                                    ?: members.first(),
                                onValueChange = { newResponsibleId ->
                                    viewModel.evoke(CreateTasksAction.UpdateResponsible(newResponsibleId))
                                },
                                label = { Text(text = "Responsible") }
                            )

                            Spacer(modifier = Modifier.padding(vertical = 10.dp))

                            SmartOutlinedTextField(
                                value = task.description,
                                label = { Text(text = "Description") },
                                onValueChange = { newDescription ->
                                    viewModel.evoke(CreateTasksAction.UpdateDescription(newDescription))
                                },
                                isError = errors.description.isError(),
                                errorMessage = errors.description.message,
                                singleLine = false,
                                maxLength = Constants.MAX_TASK_DESCRIPTION_LENGTH,
                                readOnly = readOnly,
                                enabled = true
                            )

                            Spacer(modifier = Modifier.padding(vertical = 15.dp))

                            Button(
                                onClick = {
                                    controller?.hide()
                                    viewModel.evoke(CreateTasksAction.CreateTask)
                                    onNavigateBack()
                                },
                                modifier = Modifier.width(250.dp),
                                shape = Shapes.small,
                                enabled = !errors.anyErrors()
                            ) {
                                Text(
                                    text = "Save",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.padding(vertical = 60.dp))
                        }
                    }
                    TaskEditPage.SubtasksPage -> {
                        AddNewSubtaskItem(title = "Add", onClick = { showCreateDialog = true })

                        if(task.subtasks.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                itemsIndexed(task.subtasks) { index, subtask ->
                                    SubtaskCreateListItem(
                                        number = index,
                                        title = subtask.title,
                                        onDelete = { id ->
                                            viewModel.evoke(CreateTasksAction.DeleteSubtask(id))
                                        }
                                    )

                                    if (task.subtasks.last() != subtask) {
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                        else{
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ){
                                Text(
                                    text = "There are no subtasks for this task",
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
            ){
                Text(text = "Task not found...", modifier = Modifier.align(Alignment.Center))
            }
        }

        if(showCreateDialog){
            CreateSubtaskDialog(
                onDismissRequest = { showCreateDialog = false },
                onConfirmation = { subtaskTitle ->
                    viewModel.evoke(CreateTasksAction.AddSubtask(subtaskTitle))
                    showCreateDialog = false
                }
            )
        }
    }
}