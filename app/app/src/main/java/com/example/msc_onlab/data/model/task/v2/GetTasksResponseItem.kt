package com.example.msc_onlab.data.model.task.v2

import androidx.compose.ui.text.toLowerCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class GetTasksResponseItem(
    val creation_date: String,
    val description: String,
    var due_date: String,
    val id: Int,
    val responsible: Responsible,
    val status: String,
    val subtasks: List<Subtask>,
    val team_id: Int,
    val title: String
)

fun GetTasksResponseItem.transformDate() {
    val inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val date = LocalDate.parse(this.due_date, inputFormatter)
    this.due_date = date.format(outputFormatter)
}
