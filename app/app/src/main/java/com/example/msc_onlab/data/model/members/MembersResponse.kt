package com.example.msc_onlab.data.model.members

data class MembersResponse(
    val `data`: List<Data>,
    val no_data: Int,
    val status: String,
    val time: String
)