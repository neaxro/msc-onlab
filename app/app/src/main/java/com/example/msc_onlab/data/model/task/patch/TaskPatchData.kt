package com.example.msc_onlab.data.model.task.patch

data class TaskPatchData(
    val creation_date: String,
    val description: String,
    val done: Boolean,
    val due_date: String,
    val responsible_id: String,
    val subtasks: List<Subtask>,
    val title: String
)