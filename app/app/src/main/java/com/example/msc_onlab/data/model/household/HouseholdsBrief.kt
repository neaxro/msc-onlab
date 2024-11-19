package com.example.msc_onlab.data.model.household

data class HouseholdsBrief(
    val `data`: List<Data>,
    val no_data: Int,
    val status: String,
    val time: String
)