package com.example.msc_onlab.data.model.household

data class HouseholdTasksResponse(
    val `data`: List<TaskDetailed>,
    val no_data: Int,
    val status: String,
    val time: String
)