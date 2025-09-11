package com.example.msc_onlab.data.model.task.v2.update

data class UpdateTaskData(
    val description: String,
    val due_date: String,
    val id: Int,
    val responsible_id: String,
    val status_id: Int,
    val title: String
)