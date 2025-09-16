package com.example.msc_onlab.ui.feature.tasks.subtask

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.Subtask
import com.example.msc_onlab.ui.feature.common.AddNewSubtaskItem
import com.example.msc_onlab.ui.feature.common.SubtaskBriefListItem

@Composable
fun ViewSubtasksScreen(
    viewModel: SubtasksScreenViewModel = hiltViewModel(),
    task: GetTasksResponseItem?,
    requestRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    if(task != null){
        AddNewSubtaskItem(title = "Add", onClick = { showCreateDialog = true })
    }

    if(task != null && task.subtasks.isNotEmpty()) {


        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(task.subtasks) { subtask ->
                SubtaskBriefListItem(
                    id = subtask.id,
                    isDone = subtask.done,
                    title = subtask.title,
                    onDoneButton = { id, isDone ->
                        viewModel.evoke(SubtaskActions.Update(subtask.copy(done = isDone)))
                        requestRefresh()
                    },
                    onDelete = { id ->
                        viewModel.evoke(SubtaskActions.Delete(id))
                        requestRefresh()
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
            modifier = modifier.fillMaxSize()
        ){
            Text(
                text = "There are no subtasks for this task",
                fontWeight = FontWeight.Light,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    if(showCreateDialog){
        CreateSubtaskDialog(
            onDismissRequest = { showCreateDialog = false },
            onConfirmation = { subtaskTitle ->
                viewModel.evoke(SubtaskActions.Create(title = subtaskTitle, taskId = task!!.id))
                requestRefresh()
                showCreateDialog = false
            }
        )
    }
}