package com.example.msc_onlab.data.model.household

data class DataXXXX(
    val _id: IdXXX,
    val creation_date: String,
    val description: String,
    val done: Boolean,
    val due_date: String,
    val responsible: Responsible,
    val subtasks: List<SubtaskX>,
    val title: String
)