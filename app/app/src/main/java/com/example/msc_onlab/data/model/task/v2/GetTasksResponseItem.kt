package com.example.msc_onlab.data.model.task.v2

import androidx.compose.ui.text.toLowerCase

data class GetTasksResponseItem(
    val creation_date: String,
    val description: String,
    val due_date: String,
    val id: Int,
    val responsible: Responsible,
    val status: String,
    val subtasks: List<Subtask>,
    val team_id: Int,
    val title: String
)

fun GetTasksResponseItem.isDone(): Boolean {
    return this.status.lowercase() == "done"
}
