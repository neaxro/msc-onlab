package com.example.msc_onlab.data.model.task.create

data class CreateTaskData(
    val description: String,
    val due_date: String,
    val responsible_id: String,
    val subtasks: List<Subtask>,
    val title: String
)