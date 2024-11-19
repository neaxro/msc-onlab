package com.example.msc_onlab.data.model.household

data class Task(
    val _id: IdX,
    val creation_date: String,
    val description: String,
    val done: Boolean,
    val due_date: String,
    val responsible_id: ResponsibleId,
    val subtasks: List<Subtask>,
    val title: String
)