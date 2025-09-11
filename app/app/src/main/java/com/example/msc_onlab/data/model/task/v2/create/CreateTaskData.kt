package com.example.msc_onlab.data.model.task.v2.create

data class CreateTaskData(
    val description: String = "",
    val due_date: String = "",
    val responsible_id: String = "",
    val subtasks: List<Subtask> = listOf(),
    val team_id: Int = 0,
    val title: String = ""
)